package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.ui.common.UiText

sealed interface WorkoutExecutionEvent {
    data object Close : WorkoutExecutionEvent
    data class ShowSuccess(val text: UiText) : WorkoutExecutionEvent
    data class ShowError(val text: UiText) : WorkoutExecutionEvent
    data object Vibrate: WorkoutExecutionEvent
}