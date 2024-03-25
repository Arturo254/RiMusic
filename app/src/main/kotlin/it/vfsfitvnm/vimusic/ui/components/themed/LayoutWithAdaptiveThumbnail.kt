package it.vfsfitvnm.vimusic.ui.components.themed

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import it.vfsfitvnm.vimusic.ui.styling.px
import it.vfsfitvnm.vimusic.ui.styling.shimmer
import it.vfsfitvnm.vimusic.utils.isLandscape
import it.vfsfitvnm.vimusic.utils.thumbnail
import it.vfsfitvnm.vimusic.utils.toast
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


@Composable
inline fun LayoutWithAdaptiveThumbnail(
    thumbnailContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val isLandscape = isLandscape

    if (isLandscape) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            thumbnailContent()
            content()
        }
    } else {
        content()
    }
}

@UnstableApi
fun adaptiveThumbnailContent(
    isLoading: Boolean,
    url: String?,
    shape: Shape? = null
): @Composable () -> Unit = {
    val (colorPalette, _, thumbnailShape) = LocalAppearance.current

    BoxWithConstraints(contentAlignment = Alignment.Center) {
        val thumbnailSizeDp = if (isLandscape) (maxHeight - 128.dp) else (maxWidth - 64.dp)
        val thumbnailSizePx = thumbnailSizeDp.px
        val context = LocalContext.current

        val modifier = Modifier
            .padding(all = 16.dp)
            .clip(shape ?: thumbnailShape)
            .size(thumbnailSizeDp)

        //val painter = rememberAsyncImagePainter(
        //    model = url?.thumbnail(thumbnailSizePx),
        //)

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .size(coil.size.Size.ORIGINAL)
                .build()
        )

        var bitmap = remember<Bitmap?> {
            null
        }
        val imageState = painter.state
        /*
        val scaledBitmap = remember {
            bitmap?.asImageBitmap()?.let {
                Bitmap.createScaledBitmap(
                    it.asAndroidBitmap(),
                    thumbnailSizePx,
                    thumbnailSizePx,
                    false
                )
                    //.asImageBitmap()
            }
        }
         */

        var isExporting by remember {
            mutableStateOf(false)
        }

        val exportLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("image/png")) { uri ->
                if (uri == null) return@rememberLauncherForActivityResult

                context.applicationContext.contentResolver.openOutputStream(uri)
                    ?.use { outputStream ->
                        if (imageState is AsyncImagePainter.State.Success) {
                            bitmap = imageState.result.drawable.toBitmap()
                            try {
                                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                outputStream.flush()
                                outputStream.close()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                context.toast("Error")
                            }
                        } else context.toast("Error")
                    }

            }

        /*
        if (isExporting) {
            InputTextDialog(
                onDismiss = {
                    isExporting = false
                },
                title = stringResource(R.string.enter_the_playlist_name),
                value = "",
                placeholder = stringResource(R.string.enter_the_playlist_name),
                setValue = { text ->
                    if(isExporting) {
                        try {
                            @SuppressLint("SimpleDateFormat")
                            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
                            exportLauncher.launch("imageCover${text.take(20)}_${dateFormat.format(
                                Date()
                            )}")
                        } catch (e: ActivityNotFoundException) {
                            context.toast("Couldn't find an application to create documents")
                        }
                    }

                }
            )
        }
         */

        if (isLoading) {
            Spacer(
                modifier = modifier
                    .shimmer()
                    .background(colorPalette.shimmer)
            )
        } else {
            AsyncImage(
                model = url?.thumbnail(thumbnailSizePx),
                contentDescription = null,
                modifier = modifier
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(0.2f)
            ) {
                HeaderIconButton(
                    icon = R.drawable.image_download,
                    color = colorPalette.text,
                    onClick = {
                        //isExporting = true
                        try {
                            @SuppressLint("SimpleDateFormat")
                            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
                            exportLauncher.launch("ImageCover_${dateFormat.format(
                                Date()
                            )}")
                        } catch (e: ActivityNotFoundException) {
                            context.toast("Couldn't find an application to create documents")
                        }
                    },
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}
