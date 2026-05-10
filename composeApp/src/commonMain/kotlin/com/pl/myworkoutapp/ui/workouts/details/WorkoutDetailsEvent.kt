package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.common.UiText

sealed interface WorkoutDetailsEvent {
    data object Close : WorkoutDetailsEvent
    data class ShowSuccess(val text: UiText) : WorkoutDetailsEvent
    data class ShowError(val text: UiText) : WorkoutDetailsEvent
    data class NavToWorkoutExecution(val workoutId: WorkoutId) : WorkoutDetailsEvent
    data object Vibrate: WorkoutDetailsEvent
}