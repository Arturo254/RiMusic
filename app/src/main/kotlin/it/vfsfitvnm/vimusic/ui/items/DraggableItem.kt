package it.vfsfitvnm.vimusic.ui.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import kotlin.math.roundToInt


enum class DragAnchors {
    Start,
    Center,
    End,
}

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableItem(
    state: AnchoredDraggableState<DragAnchors>,
    content: @Composable BoxScope.() -> Unit,
    startAction: @Composable (BoxScope.() -> Unit)? = {},
    endAction: @Composable (BoxScope.() -> Unit)? = {},
    draggableActive: Boolean = true,
    onHorizontalSwipeWhenActionDisabled: () -> Unit
) {
    //val (colorPalette, typography, thumbnailShape) = LocalAppearance.current
    Box(
        modifier = Modifier
            //.padding(16.dp)
            .fillMaxWidth()
            //.background(colorPalette.background2)
            //.height(100.dp)
            //.clip(thumbnailShape)
    ) {

        endAction?.let {
            endAction()
        }

        startAction?.let {
            startAction()
        }
        if (draggableActive)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .offset {
                        IntOffset(
                            x = -state
                                .requireOffset()
                                .roundToInt(),
                            y = 0,
                        )
                    }
                    .anchoredDraggable(state, Orientation.Horizontal, reverseDirection = true),
                content = content
            )
        else {
            //content()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { change, dragAmount ->
                                //deltaX = dragAmount
                            },

                            onDragEnd = {
                                onHorizontalSwipeWhenActionDisabled()
                            }
                        )
                    },
                content = content
            )
        }
    }
}