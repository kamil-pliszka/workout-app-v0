package com.pl.myworkoutapp.ui.common

data class DragDropEvent(
    val draggedKey: Int,
    val targetKey: Int,
    val position: DropPosition
)