package it.vfsfitvnm.vimusic.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import it.vfsfitvnm.vimusic.LocalPlayerAwareWindowInsets
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.enums.BackgroundProgress
import it.vfsfitvnm.vimusic.enums.ColorPaletteMode
import it.vfsfitvnm.vimusic.enums.ColorPaletteName
import it.vfsfitvnm.vimusic.enums.FontType
import it.vfsfitvnm.vimusic.enums.HomeScreenTabs
import it.vfsfitvnm.vimusic.enums.NavigationBarPosition
import it.vfsfitvnm.vimusic.enums.PlayerPlayButtonType
import it.vfsfitvnm.vimusic.enums.PlayerThumbnailSize
import it.vfsfitvnm.vimusic.enums.PlayerTimelineType
import it.vfsfitvnm.vimusic.enums.PlayerVisualizerType
import it.vfsfitvnm.vimusic.enums.ThumbnailRoundness
import it.vfsfitvnm.vimusic.enums.UiType
import it.vfsfitvnm.vimusic.ui.components.themed.HeaderIconButton
import it.vfsfitvnm.vimusic.ui.components.themed.HeaderWithIcon
import it.vfsfitvnm.vimusic.ui.components.themed.IconButton
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import it.vfsfitvnm.vimusic.ui.styling.favoritesIcon
import it.vfsfitvnm.vimusic.utils.UiTypeKey
import it.vfsfitvnm.vimusic.utils.applyFontPaddingKey
import it.vfsfitvnm.vimusic.utils.colorPaletteModeKey
import it.vfsfitvnm.vimusic.utils.colorPaletteNameKey
import it.vfsfitvnm.vimusic.utils.contentWidthKey
import it.vfsfitvnm.vimusic.utils.disableIconButtonOnTopKey
import it.vfsfitvnm.vimusic.utils.disablePlayerHorizontalSwipeKey
import it.vfsfitvnm.vimusic.utils.disableScrollingTextKey
import it.vfsfitvnm.vimusic.utils.effectRotationKey
import it.vfsfitvnm.vimusic.utils.fontTypeKey
import it.vfsfitvnm.vimusic.utils.indexNavigationTabKey
import it.vfsfitvnm.vimusic.utils.isAtLeastAndroid13
import it.vfsfitvnm.vimusic.utils.isGradientBackgroundEnabledKey
import it.vfsfitvnm.vimusic.utils.isShowingThumbnailInLockscreenKey
import it.vfsfitvnm.vimusic.utils.lastPlayerPlayButtonTypeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerThumbnailSizeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerTimelineTypeKey
import it.vfsfitvnm.vimusic.utils.lastPlayerVisualizerTypeKey
import it.vfsfitvnm.vimusic.utils.navigationBarPositionKey
import it.vfsfitvnm.vimusic.utils.playerPlayButtonTypeKey
import it.vfsfitvnm.vimusic.utils.playerThumbnailSizeKey
import it.vfsfitvnm.vimusic.utils.playerTimelineTypeKey
import it.vfsfitvnm.vimusic.utils.playerVisualizerTypeKey
import it.vfsfitvnm.vimusic.utils.preferences
import it.vfsfitvnm.vimusic.utils.rememberPreference
import it.vfsfitvnm.vimusic.utils.secondary
import it.vfsfitvnm.vimusic.utils.semiBold
import it.vfsfitvnm.vimusic.utils.showButtonPlayerAddToPlaylistKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerArrowKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerDownloadKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerLoopKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerLyricsKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerMenuKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerShuffleKey
import it.vfsfitvnm.vimusic.utils.showButtonPlayerSleepTimerKey
import it.vfsfitvnm.vimusic.utils.showCachedPlaylistKey
import it.vfsfitvnm.vimusic.utils.showDownloadButtonBackgroundPlayerKey
import it.vfsfitvnm.vimusic.utils.showDownloadedPlaylistKey
import it.vfsfitvnm.vimusic.utils.showFavoritesPlaylistKey
import it.vfsfitvnm.vimusic.utils.showLikeButtonBackgroundPlayerKey
import it.vfsfitvnm.vimusic.utils.showMyTopPlaylistKey
import it.vfsfitvnm.vimusic.utils.showOnDevicePlaylistKey
import it.vfsfitvnm.vimusic.utils.backgroundProgressKey
import it.vfsfitvnm.vimusic.utils.showPlaylistsKey
import it.vfsfitvnm.vimusic.utils.showTotalTimeQueueKey
import it.vfsfitvnm.vimusic.utils.thumbnailRoundnessKey
import it.vfsfitvnm.vimusic.utils.thumbnailTapEnabledKey


@ExperimentalAnimationApi
@UnstableApi
@Composable
fun AppearanceSettings() {

    var isShowingThumbnailInLockscreen by rememberPreference(
        isShowingThumbnailInLockscreenKey,
        true
    )

    var playerPlayButtonType by rememberPreference(playerPlayButtonTypeKey, PlayerPlayButtonType.Rectangular)

    var lastPlayerPlayButtonType by rememberPreference(lastPlayerPlayButtonTypeKey, PlayerPlayButtonType.Rectangular)
    var uiType  by rememberPreference(UiTypeKey, UiType.RiMusic)
    var disablePlayerHorizontalSwipe by rememberPreference(disablePlayerHorizontalSwipeKey, false)

    var disableScrollingText by rememberPreference(disableScrollingTextKey, false)
    var showLikeButtonBackgroundPlayer by rememberPreference(showLikeButtonBackgroundPlayerKey, true)
    var showDownloadButtonBackgroundPlayer by rememberPreference(showDownloadButtonBackgroundPlayerKey, true)
    var playerVisualizerType by rememberPreference(playerVisualizerTypeKey, PlayerVisualizerType.Disabled)
    var playerTimelineType by rememberPreference(playerTimelineTypeKey, PlayerTimelineType.Default)
    var playerThumbnailSize by rememberPreference(playerThumbnailSizeKey, PlayerThumbnailSize.Medium)

    var effectRotationEnabled by rememberPreference(effectRotationKey, true)

    var thumbnailTapEnabled by rememberPreference(thumbnailTapEnabledKey, false)


    var showButtonPlayerAddToPlaylist by rememberPreference(showButtonPlayerAddToPlaylistKey, true)
    var showButtonPlayerArrow by rememberPreference(showButtonPlayerArrowKey, false)
    var showButtonPlayerDownload by rememberPreference(showButtonPlayerDownloadKey, true)
    var showButtonPlayerLoop by rememberPreference(showButtonPlayerLoopKey, true)
    var showButtonPlayerLyrics by rememberPreference(showButtonPlayerLyricsKey, true)
    var showButtonPlayerShuffle by rememberPreference(showButtonPlayerShuffleKey, true)
    var showButtonPlayerSleepTimer by rememberPreference(showButtonPlayerSleepTimerKey, false)
    var showButtonPlayerMenu by rememberPreference(showButtonPlayerMenuKey, false)

    val context = LocalContext.current
    val navigationBarPosition by rememberPreference(navigationBarPositionKey, NavigationBarPosition.Left)
    val contentWidth = context.preferences.getFloat(contentWidthKey,0.8f)

    var showFavoritesPlaylist by rememberPreference(showFavoritesPlaylistKey, true)
    var showCachedPlaylist by rememberPreference(showCachedPlaylistKey, true)
    var showMyTopPlaylist by rememberPreference(showMyTopPlaylistKey, true)
    var showDownloadedPlaylist by rememberPreference(showDownloadedPlaylistKey, true)
    var showOnDevicePlaylist by rememberPreference(showOnDevicePlaylistKey, true)
    var showPlaylists by rememberPreference(showPlaylistsKey, true)
    var isGradientBackgroundEnabled by rememberPreference(isGradientBackgroundEnabledKey, false)
    var showTotalTimeQueue by rememberPreference(showTotalTimeQueueKey, true)
    var backgroundProgress by rememberPreference(backgroundProgressKey, BackgroundProgress.MiniPlayer)

    val (colorPalette, typography, thumbnailShape) = LocalAppearance.current
    var searching by rememberSaveable { mutableStateOf(false) }
    var filter: String? by rememberSaveable { mutableStateOf(null) }
    // var filterCharSequence: CharSequence
    var filterCharSequence: CharSequence = filter.toString()
    var thumbnailRoundness by rememberPreference(
        thumbnailRoundnessKey,
        ThumbnailRoundness.Heavy
    )

    Column(
        modifier = Modifier
            .background(colorPalette.background0)
            //.fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(if (navigationBarPosition == NavigationBarPosition.Left) 1f else contentWidth)
            .verticalScroll(rememberScrollState())
            .padding(
                LocalPlayerAwareWindowInsets.current
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                    .asPaddingValues()
            )
    ) {
        HeaderWithIcon(
            title = stringResource(R.string.player_appearance),
            iconId = R.drawable.color_palette,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )

        HeaderIconButton(
            modifier = Modifier.padding(horizontal = 5.dp),
            onClick = { searching = !searching },
            icon = R.drawable.search_circle,
            color = colorPalette.text,
            iconSize = 24.dp
        )
        /*   Search   */
        Row (
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(visible = searching) {
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                LaunchedEffect(searching) {
                    focusRequester.requestFocus()
                }

                BasicTextField(
                    value = filter ?: "",
                    onValueChange = { filter = it },
                    textStyle = typography.xs.semiBold,
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (filter.isNullOrBlank()) filter = ""
                        focusManager.clearFocus()
                    }),
                    cursorBrush = SolidColor(colorPalette.text),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp)
                        ) {
                            IconButton(
                                onClick = {},
                                icon = R.drawable.search,
                                color = colorPalette.favoritesIcon,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(16.dp)
                            )
                        }
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 30.dp)
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = filter?.isEmpty() ?: true,
                                enter = fadeIn(tween(100)),
                                exit = fadeOut(tween(100)),
                            ) {
                                BasicText(
                                    text = stringResource(R.string.search),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = typography.xs.semiBold.secondary.copy(color = colorPalette.textDisabled),
                                )
                            }

                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(
                            colorPalette.background4,
                            shape = thumbnailRoundness.shape()
                        )
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (!it.hasFocus) {
                                keyboardController?.hide()
                                if (filter?.isBlank() == true) {
                                    filter = null
                                    searching = false
                                }
                            }
                        }
                )
            }
        }
        /*  Search  */

        //SettingsGroupSpacer()
        //SettingsEntryGroupText(stringResource(R.string.user_interface))

        //SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.player))

        if (filter.isNullOrBlank() || stringResource(R.string.player_thumbnail_size).contains(filterCharSequence,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.player_thumbnail_size),
                selectedValue = playerThumbnailSize,
                onValueSelected = { playerThumbnailSize = it },
                valueText = {
                    when (it) {
                        PlayerThumbnailSize.Small -> stringResource(R.string.small)
                        PlayerThumbnailSize.Medium -> stringResource(R.string.medium)
                        PlayerThumbnailSize.Big -> stringResource(R.string.big)
                        PlayerThumbnailSize.Biggest -> stringResource(R.string.biggest)
                    }
                }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.thumbnail_roundness).contains(filterCharSequence,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.thumbnail_roundness),
                selectedValue = thumbnailRoundness,
                onValueSelected = { thumbnailRoundness = it },
                trailingContent = {
                    Spacer(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorPalette.accent,
                                shape = thumbnailRoundness.shape()
                            )
                            .background(
                                color = colorPalette.background1,
                                shape = thumbnailRoundness.shape()
                            )
                            .size(36.dp)
                    )
                },
                valueText = {
                    when (it) {
                        ThumbnailRoundness.None -> stringResource(R.string.none)
                        ThumbnailRoundness.Light -> stringResource(R.string.light)
                        ThumbnailRoundness.Heavy -> stringResource(R.string.heavy)
                        ThumbnailRoundness.Medium -> stringResource(R.string.medium)
                    }
                }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.use_gradient_background).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.use_gradient_background),
                text = "",
                isChecked = isGradientBackgroundEnabled,
                onCheckedChange = { isGradientBackgroundEnabled = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.background_progress_bar).contains(filterCharSequence,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.background_progress_bar),
                selectedValue = backgroundProgress,
                onValueSelected = {
                    backgroundProgress = it
                },
                valueText = {
                    when (it) {
                        BackgroundProgress.Player -> stringResource(R.string.player)
                        BackgroundProgress.MiniPlayer -> stringResource(R.string.minimized_player)
                        BackgroundProgress.Both -> stringResource(R.string.both)
                    }
                },
            )

        if (filter.isNullOrBlank() || stringResource(R.string.show_total_time_of_queue).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.show_total_time_of_queue),
                text = "",
                isChecked = showTotalTimeQueue,
                onCheckedChange = { showTotalTimeQueue = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.disable_scrolling_text).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.disable_scrolling_text),
                text = stringResource(R.string.scrolling_text_is_used_for_long_texts),
                isChecked = disableScrollingText,
                onCheckedChange = { disableScrollingText = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.disable_horizontal_swipe).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.disable_horizontal_swipe),
                text = stringResource(R.string.disable_song_switching_via_swipe),
                isChecked = disablePlayerHorizontalSwipe,
                onCheckedChange = { disablePlayerHorizontalSwipe = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.timeline).contains(filterCharSequence,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.timeline),
                selectedValue = playerTimelineType,
                onValueSelected = { playerTimelineType = it },
                valueText = {
                    when (it) {
                        PlayerTimelineType.Default -> stringResource(R.string._default)
                        PlayerTimelineType.Wavy -> stringResource(R.string.wavy_timeline)
                        PlayerTimelineType.BodiedBar -> stringResource(R.string.bodied_bar)
                        PlayerTimelineType.PinBar -> stringResource(R.string.pin_bar)
                    }
                }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.play_button).contains(filterCharSequence,true))
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.play_button),
                selectedValue = playerPlayButtonType,
                onValueSelected = {
                    playerPlayButtonType = it
                    lastPlayerPlayButtonType = it
                },
                valueText = {
                    when (it) {
                        PlayerPlayButtonType.Disabled -> stringResource(R.string.vt_disabled)
                        PlayerPlayButtonType.Default -> stringResource(R.string._default)
                        PlayerPlayButtonType.Rectangular -> stringResource(R.string.rectangular)
                        PlayerPlayButtonType.Square -> stringResource(R.string.square)
                        PlayerPlayButtonType.CircularRibbed -> stringResource(R.string.circular_ribbed)
                    }
                },
            )


        if (filter.isNullOrBlank() || stringResource(R.string.player_rotating_buttons).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.player_rotating_buttons),
                text = stringResource(R.string.player_enable_rotation_buttons),
                isChecked = effectRotationEnabled,
                onCheckedChange = { effectRotationEnabled = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.visualizer).contains(filterCharSequence,true)) {
            EnumValueSelectorSettingsEntry(
                title = stringResource(R.string.visualizer),
                selectedValue = playerVisualizerType,
                onValueSelected = { playerVisualizerType = it },
                valueText = {
                    when (it) {
                        PlayerVisualizerType.Fancy -> stringResource(R.string.vt_fancy)
                        PlayerVisualizerType.Circular -> stringResource(R.string.vt_circular)
                        PlayerVisualizerType.Disabled -> stringResource(R.string.vt_disabled)
                        PlayerVisualizerType.Stacked -> stringResource(R.string.vt_stacked)
                        PlayerVisualizerType.Oneside -> stringResource(R.string.vt_one_side)
                        PlayerVisualizerType.Doubleside -> stringResource(R.string.vt_double_side)
                        PlayerVisualizerType.DoublesideCircular -> stringResource(R.string.vt_double_side_circular)
                        PlayerVisualizerType.Full -> stringResource(R.string.vt_full)
                    }
                }
            )
            ImportantSettingsDescription(text = stringResource(R.string.visualizer_require_mic_permission))
        }

        if (filter.isNullOrBlank() || stringResource(R.string.toggle_lyrics).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.toggle_lyrics),
                text = stringResource(R.string.by_tapping_on_the_thumbnail),
                isChecked = thumbnailTapEnabled,
                onCheckedChange = { thumbnailTapEnabled = it }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.player_action_bar))

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_download_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_download_button),
                text = "",
                isChecked = showButtonPlayerDownload,
                onCheckedChange = { showButtonPlayerDownload = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_add_to_playlist_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_add_to_playlist_button),
                text = "",
                isChecked = showButtonPlayerAddToPlaylist,
                onCheckedChange = { showButtonPlayerAddToPlaylist = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_loop_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_loop_button),
                text = "",
                isChecked = showButtonPlayerLoop,
                onCheckedChange = { showButtonPlayerLoop = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_shuffle_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_shuffle_button),
                text = "",
                isChecked = showButtonPlayerShuffle,
                onCheckedChange = { showButtonPlayerShuffle = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_lyrics_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_lyrics_button),
                text = "",
                isChecked = showButtonPlayerLyrics,
                onCheckedChange = { showButtonPlayerLyrics = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_sleep_timer_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_sleep_timer_button),
                text = "",
                isChecked = showButtonPlayerSleepTimer,
                onCheckedChange = { showButtonPlayerSleepTimer = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_arrow_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_arrow_button),
                text = "",
                isChecked = showButtonPlayerArrow,
                onCheckedChange = { showButtonPlayerArrow = it }
            )

        if (filter.isNullOrBlank() || stringResource(R.string.action_bar_show_menu_button).contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = stringResource(R.string.action_bar_show_menu_button),
                text = "",
                isChecked = showButtonPlayerMenu,
                onCheckedChange = { showButtonPlayerMenu = it }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.playlists).uppercase())

        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.favorites)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.favorites)}",
                text = "",
                isChecked = showFavoritesPlaylist,
                onCheckedChange = { showFavoritesPlaylist = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.cached)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.cached)}",
                text = "",
                isChecked = showCachedPlaylist,
                onCheckedChange = { showCachedPlaylist = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.downloaded)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.downloaded)}",
                text = "",
                isChecked = showDownloadedPlaylist,
                onCheckedChange = { showDownloadedPlaylist = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.my_playlist_top)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.my_playlist_top)}",
                text = "",
                isChecked = showMyTopPlaylist,
                onCheckedChange = { showMyTopPlaylist = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.on_device)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.on_device)}",
                text = "",
                isChecked = showOnDevicePlaylist,
                onCheckedChange = { showOnDevicePlaylist = it }
            )
        if (filter.isNullOrBlank() || "${stringResource(R.string.show)} ${stringResource(R.string.playlists)}".contains(filterCharSequence,true))
            SwitchSettingEntry(
                title = "${stringResource(R.string.show)} ${stringResource(R.string.playlists)}",
                text = "",
                isChecked = showPlaylists,
                onCheckedChange = { showPlaylists = it }
            )

        SettingsGroupSpacer()
        SettingsEntryGroupText(title = stringResource(R.string.background_player))

        if (filter.isNullOrBlank() || stringResource(R.string.show_favorite_button).contains(filterCharSequence,true)) {
            SwitchSettingEntry(
                title = stringResource(R.string.show_favorite_button),
                text = stringResource(R.string.show_favorite_button_in_lock_screen_and_notification_area),
                isChecked = showLikeButtonBackgroundPlayer,
                onCheckedChange = { showLikeButtonBackgroundPlayer = it }
            )
            ImportantSettingsDescription(text = stringResource(R.string.restarting_rimusic_is_required))
        }
        if (filter.isNullOrBlank() || stringResource(R.string.show_download_button).contains(filterCharSequence,true)) {
            SwitchSettingEntry(
                title = stringResource(R.string.show_download_button),
                text = stringResource(R.string.show_download_button_in_lock_screen_and_notification_area),
                isChecked = showDownloadButtonBackgroundPlayer,
                onCheckedChange = { showDownloadButtonBackgroundPlayer = it }
            )

            ImportantSettingsDescription(text = stringResource(R.string.restarting_rimusic_is_required))
        }

        //SettingsGroupSpacer()
        //SettingsEntryGroupText(title = stringResource(R.string.text))


        if (filter.isNullOrBlank() || stringResource(R.string.show_song_cover).contains(filterCharSequence,true))
        if (!isAtLeastAndroid13) {
            SettingsGroupSpacer()

            SettingsEntryGroupText(title = stringResource(R.string.lockscreen))

            SwitchSettingEntry(
                title = stringResource(R.string.show_song_cover),
                text = stringResource(R.string.use_song_cover_on_lockscreen),
                isChecked = isShowingThumbnailInLockscreen,
                onCheckedChange = { isShowingThumbnailInLockscreen = it }
            )
        }
    }
}
