package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

class DragDropState(
    val listState: LazyListState,
    val onMove: (Int, Int) -> Unit
) {

    var draggingIndex by mutableStateOf<Int?>(null)
        private set

    private var dragOffsetY by mutableStateOf(0f)

    fun onDragStart(index: Int) {
        draggingIndex = index
        dragOffsetY = 0f
    }

    fun onDrag(delta: Float) {
        dragOffsetY += delta
    }

    fun onDragEnd() {
        val from = draggingIndex ?: return
        val to = calculateTargetIndex(from)

        draggingIndex = null
        dragOffsetY = 0f

        if (from != to) {
            onMove(from, to)
        }
    }

    fun onDragCancel() {
        draggingIndex = null
        dragOffsetY = 0f
    }

    fun offsetFor(index: Int): Float {
        return if (index == draggingIndex) dragOffsetY else 0f
    }

    private fun calculateTargetIndex(fromIndex: Int): Int {
        val layout = listState.layoutInfo.visibleItemsInfo

        val fromItem = layout.find { it.index == fromIndex }
            ?: return fromIndex

        val centerY = fromItem.offset + dragOffsetY + fromItem.size / 2

        return layout.minByOrNull { item ->
            val itemCenter = item.offset + item.size / 2
            kotlin.math.abs(itemCenter - centerY)
        }?.index ?: fromIndex
    }
}

@Composable
fun rememberDragDropState(
    listState: LazyListState,
    onMove: (Int, Int) -> Unit
): DragDropState {
    return remember {
        DragDropState(listState, onMove)
    }
}
