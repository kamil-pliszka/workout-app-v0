package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex

@Composable
fun LazyItemScope.DraggableItem(
    dragDropState: DragDropState,
    index: Int,
    content: @Composable () -> Unit
) {
    val isDragging = dragDropState.draggingIndex == index

    val offsetY = dragDropState.offsetFor(index)

    Box(
        modifier = Modifier
            .animateItem()
            .offset {
                if (isDragging) {
                    IntOffset(0, offsetY.toInt())
                } else {
                    IntOffset.Zero
                }
            }
            .zIndex(if (isDragging) 1f else 0f)
    ) {
        content()
    }
}