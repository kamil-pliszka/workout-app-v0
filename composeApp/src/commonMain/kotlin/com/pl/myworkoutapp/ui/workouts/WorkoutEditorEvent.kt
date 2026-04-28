package com.pl.myworkoutapp.ui.workouts


sealed interface WorkoutEditorEvent {
    data class ScrollEditorTo(val index: Int) : WorkoutEditorEvent
}