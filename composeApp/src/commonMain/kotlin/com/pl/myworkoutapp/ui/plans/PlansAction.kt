package com.pl.myworkoutapp.ui.plans

import com.pl.myworkoutapp.domain.model.workout.WorkoutId

sealed interface PlansAction {
    data class OnStartPlan(val planId: String) : PlansAction
    data class OnPageChanged(val index: Int) : PlansAction
    data class NavToWorkout(val workoutId: WorkoutId) : PlansAction
}