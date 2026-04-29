package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import com.pl.myworkoutapp.ui.workouts.*

class DragDropState(
    val listState: LazyListState,
    private val itemsProvider: () -> List<WorkoutUiItem>,
    private val onDrop: (DragDropEvent) -> Unit
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
        val draggedIndex = draggingIndex ?: return
        val event = calculateDropEvent(draggedIndex)

        draggingIndex = null
        dragOffsetY = 0f

        if (event != null && event.draggedKey != event.targetKey) {
            onDrop(event)
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

    private fun calculateDropEvent(draggedIndex: Int): DragDropEvent? {
        val items = itemsProvider()
        val layout = listState.layoutInfo.visibleItemsInfo

        val draggedLayout = layout.find { it.index == draggedIndex }
            ?: return null

        val draggedItem = items.getOrNull(draggedIndex)
            ?: return null

        val draggedCenterY = draggedLayout.offset + dragOffsetY + draggedLayout.size / 2f

        val targetLayout = layout
            .filter { it.index != draggedIndex }
            .minByOrNull { visibleItem ->
                val center = visibleItem.offset + visibleItem.size / 2f
                kotlin.math.abs(center - draggedCenterY)
            } ?: return null

        val targetIndex = targetLayout.index
        val targetItem = items.getOrNull(targetIndex)
            ?: return null

        val relativeY = draggedCenterY - targetLayout.offset
        val height = targetLayout.size.toFloat()

        val dropPosition = resolveDropPosition(
            targetItem,
            relativeY,
            height
        )

        return DragDropEvent(
            draggedKey = draggedItem.key,
            targetKey = targetItem.key,
            position = dropPosition
        )
    }

    private fun resolveDropPosition(
        targetItem: WorkoutUiItem,
        relativeY: Float,
        height: Float
    ): DropPosition {
        return when (targetItem) {
            is ExerciseUiItem -> {
                if (relativeY < height * 0.5f) DropPosition.BEFORE
                else DropPosition.AFTER
            }

            is CircuitUiItem -> when {
                relativeY < height * 0.25f -> DropPosition.BEFORE
                relativeY > height * 0.75f -> DropPosition.AFTER
                else -> DropPosition.INSIDE
            }
        }
    }
}

@Composable
fun rememberDragDropState(
    listState: LazyListState,
    itemsProvider: () -> List<WorkoutUiItem>,
    onDrop: (DragDropEvent) -> Unit
): DragDropState {
    val currentItemsProvider by rememberUpdatedState(itemsProvider)
    val currentOnDrop by rememberUpdatedState(onDrop)

    return remember(listState) {
        DragDropState(
            listState = listState,
            itemsProvider = { currentItemsProvider() },
            onDrop = { currentOnDrop(it) }
        )
    }
}