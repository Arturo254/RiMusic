package it.vfsfitvnm.vimusic.service

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.Download
import it.vfsfitvnm.vimusic.Database
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.enums.ExoPlayerMinTimeForEvent
import it.vfsfitvnm.vimusic.enums.MaxTopPlaylistItems
import it.vfsfitvnm.vimusic.models.Album
import it.vfsfitvnm.vimusic.models.Artist
import it.vfsfitvnm.vimusic.models.PlaylistPreview
import it.vfsfitvnm.vimusic.models.Song
import it.vfsfitvnm.vimusic.models.SongWithContentLength
import it.vfsfitvnm.vimusic.query
import it.vfsfitvnm.vimusic.transaction
import it.vfsfitvnm.vimusic.utils.MaxTopPlaylistItemsKey
import it.vfsfitvnm.vimusic.utils.asMediaItem
import it.vfsfitvnm.vimusic.utils.exoPlayerMinTimeForEventKey
import it.vfsfitvnm.vimusic.utils.forcePlayAtIndex
import it.vfsfitvnm.vimusic.utils.forceSeekToNext
import it.vfsfitvnm.vimusic.utils.forceSeekToPrevious
import it.vfsfitvnm.vimusic.utils.getEnum
import it.vfsfitvnm.vimusic.utils.intent
import it.vfsfitvnm.vimusic.utils.preferences
import it.vfsfitvnm.vimusic.utils.skipSilenceKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlayerMediaBrowserService : MediaBrowserServiceCompat(), ServiceConnection {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var lastSongs = emptyList<Song>()

    private var bound = false


    override fun onDestroy() {
        if (bound) {
            unbindService(this)
        }
        super.onDestroy()
    }

    @UnstableApi
    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        if (service is PlayerService.Binder) {
            bound = true
            sessionToken = service.mediaSession.sessionToken
            service.mediaSession.setCallback(SessionCallback(service, service.cache))
        }
    }

    override fun onServiceDisconnected(name: ComponentName) = Unit

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        bindService(intent<PlayerService>(), this, Context.BIND_AUTO_CREATE)
        return BrowserRoot(
            MediaId.root,
            //bundleOf("android.media.browse.CONTENT_STYLE_BROWSABLE_HINT" to 1)
            Bundle().apply {
                //putBoolean(MEDIA_SEARCH_SUPPORTED, true)
                putBoolean(CONTENT_STYLE_SUPPORTED, true)
                putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID)
                putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST)
            }
        )
        /*
        return if (clientUid == Process.myUid()
            || clientUid == Process.SYSTEM_UID
            || clientPackageName == "com.google.android.projection.gearhead"
        ) {
            bindService(intent<PlayerService>(), this, Context.BIND_AUTO_CREATE)
            BrowserRoot(
                MediaId.root,
                bundleOf("android.media.browse.CONTENT_STYLE_BROWSABLE_HINT" to 1)
            )
        } else {
            null
        }
         */
    }

    @OptIn(UnstableApi::class)
    override fun onSearch(
        query: String,
        extras: Bundle?,
        result: Result<List<MediaItem>>
    ) {
        println("RiMusicMediaBrowse ${extras}")
        runBlocking(Dispatchers.IO) {
            val resultsList =
                Database
                    .favorites()
                    .first()
                    .map {
                        MediaItem(
                            MediaDescriptionCompat.Builder()
                                .setMediaId(it.id)
                                .setTitle(it.title)
                                .setIconUri(uriFor(R.drawable.musical_notes))
                                .build(),
                            MediaItem.FLAG_BROWSABLE
                        )
                    }
                    .shuffled()

            /*
        val resultsSent = mediaSource.whenReady { successfullyInitialized ->
            if (successfullyInitialized) {
                val resultsList = mediaSource.search(query, extras ?: Bundle.EMPTY)
                    .map { mediaMetadata ->
                        MediaItem(mediaMetadata.description, mediaMetadata.flag)
                    }
                result.sendResult(resultsList)
            }
        }
 */
            if (resultsList.isEmpty()) {
                result.detach()
            } else result.sendResult(resultsList)
        }
    }

     override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        runBlocking(Dispatchers.IO) {
            result.sendResult(
                when (parentId) {
                    MediaId.root -> mutableListOf(
                        songsBrowserMediaItem,
                        playlistsBrowserMediaItem,
                        albumsBrowserMediaItem,
                        artistsBrowserMediaItem
                    )

                    MediaId.songs -> Database
                        .songsByPlayTimeDesc()
                        .first()
                        .take(500)
                        .also { lastSongs = it }
                        .map { it.asBrowserMediaItem }
                        .toMutableList()
                        .apply {
                            if (isNotEmpty()) add(0, shuffleBrowserMediaItem)
                        }

                    MediaId.playlists -> Database
                        .playlistPreviewsByDateAddedDesc()
                        .first()
                        .map { it.asBrowserMediaItem }
                        .toMutableList()
                        .apply {
                            add(0, favoritesBrowserMediaItem)
                            add(1, offlineBrowserMediaItem)
                            add(2, downloadedBrowserMediaItem)
                            add(3, topBrowserMediaItem)
                            add(4, ondeviceBrowserMediaItem)

                        }

                    MediaId.albums -> Database
                        .albumsByRowIdDesc()
                        .first()
                        .map { it.asBrowserMediaItem }
                        .toMutableList()

                    MediaId.artists -> Database
                        .artistsByRowIdDesc()
                        .first()
                        .map { it.asBrowserMediaItem }
                        .toMutableList()

                    else -> mutableListOf()
                }
            )
        }
    }

    private fun uriFor(@DrawableRes id: Int) = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(id))
        .appendPath(resources.getResourceTypeName(id))
        .appendPath(resources.getResourceEntryName(id))
        .build()


    private val shuffleBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.shuffle)
                .setTitle((this as Context).resources.getString(R.string.shuffle))
                .setIconUri(uriFor(R.drawable.shuffle))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val songsBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.songs)
                .setTitle((this as Context).resources.getString(R.string.songs))
                .setIconUri(uriFor(R.drawable.musical_notes))
                .build(),
            MediaItem.FLAG_BROWSABLE
        )


    private val playlistsBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.playlists)
                .setTitle((this as Context).resources.getString(R.string.library))
                .setIconUri(uriFor(R.drawable.library))
                .build(),
            MediaItem.FLAG_BROWSABLE
        )

    private val albumsBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.albums)
                .setTitle((this as Context).resources.getString(R.string.albums))
                .setIconUri(uriFor(R.drawable.album))
                .build(),
            MediaItem.FLAG_BROWSABLE
        )

    private val artistsBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.artists)
                .setTitle((this as Context).resources.getString(R.string.artists))
                .setIconUri(uriFor(R.drawable.artists))
                .build(),
            MediaItem.FLAG_BROWSABLE
        )

    private val favoritesBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.favorites)
                .setTitle((this as Context).resources.getString(R.string.favorites))
                .setIconUri(uriFor(R.drawable.heart))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val offlineBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.offline)
                .setTitle((this as Context).resources.getString(R.string.cached))
                .setIconUri(uriFor(R.drawable.sync))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val downloadedBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.downloaded)
                .setTitle((this as Context).resources.getString(R.string.downloaded))
                .setIconUri(uriFor(R.drawable.downloaded))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val ondeviceBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.ondevice)
                .setTitle((this as Context).resources.getString(R.string.on_device))
                .setIconUri(uriFor(R.drawable.musical_notes))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val topBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.top)
                .setTitle("${(this as Context).resources.getString(R.string.my_playlist_top)} " +
                        "${preferences.getEnum(MaxTopPlaylistItemsKey,
                            MaxTopPlaylistItems.`10`).number}")
                .setIconUri(uriFor(R.drawable.trending))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val Song.asBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.forSong(id))
                .setTitle(title)
                .setSubtitle(artistsText)
                .setIconUri(thumbnailUrl?.toUri())
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val PlaylistPreview.asBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.forPlaylist(playlist.id))
                .setTitle(playlist.name)
                .setSubtitle("$songCount ${(this@PlayerMediaBrowserService as Context).resources.getString(R.string.songs)}")
                .setIconUri(uriFor(R.drawable.playlist))
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val Album.asBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.forAlbum(id))
                .setTitle(title)
                .setSubtitle(authorsText)
                .setIconUri(thumbnailUrl?.toUri())
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private val Artist.asBrowserMediaItem
        inline get() = MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(MediaId.forArtistByName(name ?: ""))
                .setTitle(name)
                //.setSubtitle()
                .setIconUri(thumbnailUrl?.toUri())
                .build(),
            MediaItem.FLAG_PLAYABLE
        )

    private inner class SessionCallback @OptIn(UnstableApi::class) constructor(
       // private val player: Player,
        private val binder: PlayerService.Binder,
        private val cache: Cache
    ) :
        MediaSessionCompat.Callback() {
        override fun onPlay() = binder.player.play()
        override fun onPause() = binder.player.pause()
        override fun onSkipToPrevious() = binder.player.forceSeekToPrevious()
        override fun onSkipToNext() = binder.player.forceSeekToNext()
        override fun onSeekTo(pos: Long) = binder.player.seekTo(pos)
        override fun onSkipToQueueItem(id: Long) = binder.player.seekToDefaultPosition(id.toInt())
        @OptIn(UnstableApi::class)
        override fun onPlayFromSearch(query: String?, extras: Bundle?) {
            if (query.isNullOrBlank()) return
            binder.playFromSearch(query)
        }



        @FlowPreview
        @ExperimentalCoroutinesApi
        @UnstableApi
        override fun onCustomAction(action: String?, extras: Bundle?) {
            if (action == "LIKE") {
                binder.toggleLike()
                binder.refreshPlayer()
            }
            if (action == "DOWNLOAD") {
                binder.toggleDownload()
                binder.refreshPlayer()
            }

            super.onCustomAction(action, extras)
        }

        @UnstableApi
        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            val data = mediaId?.split('/') ?: return
            var index = 0

            //println("RiMusicMediaBrowser data $data ")

            coroutineScope.launch {
                val mediaItems = when (data.getOrNull(0)) {
                    MediaId.shuffle -> lastSongs

                    MediaId.songs ->  data
                        .getOrNull(1)
                        ?.let { songId ->
                            index = lastSongs.indexOfFirst { it.id == songId }
                            lastSongs
                        }

                    MediaId.favorites -> Database
                        .favorites()
                        .first()
                        .shuffled()

                    MediaId.offline -> Database
                        .songsWithContentLength()
                        .first()
                        .filter { song ->
                            song.contentLength?.let {
                                cache.isCached(song.song.id, 0, it)
                            } ?: false
                        }
                        .map(SongWithContentLength::song)
                        .shuffled()

                    MediaId.ondevice -> Database
                        .songsOnDevice()
                        .first()
                        .shuffled()

                    MediaId.downloaded -> {
                        val downloads = DownloadUtil.downloads.value
                        Database.listAllSongs()
                             .filter {
                                    downloads[it.id]?.state == Download.STATE_COMPLETED
                             }
                    }

                    MediaId.top -> {
                        val maxTopSongs = preferences.getEnum(MaxTopPlaylistItemsKey,
                            MaxTopPlaylistItems.`10`).number.toInt()

                        Database.trending(maxTopSongs)
                                .first()
                    }

                    MediaId.playlists -> data
                        .getOrNull(1)
                        ?.toLongOrNull()
                        ?.let(Database::playlistWithSongs)
                        ?.first()
                        ?.songs
                        ?.shuffled()

                    MediaId.albums -> data
                        .getOrNull(1)
                        ?.let(Database::albumSongs)
                        ?.first()

                    MediaId.artists -> {
                        data
                        .getOrNull(1)
                        ?.let(Database::artistSongsByname)
                        ?.first()
                    }

                    else -> emptyList()
                }?.map(Song::asMediaItem) ?: return@launch

                withContext(Dispatchers.Main) {
                    binder.player.forcePlayAtIndex(mediaItems, index.coerceIn(0, mediaItems.size))
                }
            }
        }
    }

    private object MediaId {
        const val root = "root"
        const val songs = "songs"
        const val playlists = "playlists"
        const val albums = "albums"
        const val artists = "artists"

        const val favorites = "favorites"
        const val offline = "offline"
        const val shuffle = "shuffle"
        const val downloaded = "downloaded"
        const val ondevice = "ondevice"
        const val top = "top"

        fun forSong(id: String) = "songs/$id"
        fun forPlaylist(id: Long) = "playlists/$id"
        fun forAlbum(id: String) = "albums/$id"
        fun forArtistByName(name: String) = "artists/$name"
    }
}

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"
private const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
private const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
private const val CONTENT_STYLE_LIST = 1
private const val CONTENT_STYLE_GRID = 2