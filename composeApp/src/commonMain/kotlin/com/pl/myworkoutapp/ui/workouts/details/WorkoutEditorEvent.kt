package com.pl.myworkoutapp.ui.workouts.details


sealed interface WorkoutEditorEvent {
    data class ScrollEditorTo(val index: Int) : WorkoutEditorEvent
}