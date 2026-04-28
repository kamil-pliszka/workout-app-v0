package com.pl.myworkoutapp.ui.workouts.editor


sealed interface WorkoutEditorEvent {
    data class ScrollEditorTo(val index: Int) : WorkoutEditorEvent
}