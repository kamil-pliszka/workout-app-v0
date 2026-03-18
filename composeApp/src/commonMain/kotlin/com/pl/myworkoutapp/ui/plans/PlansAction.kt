package com.pl.myworkoutapp.ui.plans

sealed interface PlansAction {
    object OnShowDateDialogAction : PlansAction
    object OnDialogConfirmAction : PlansAction
    data class NavToWorkout(val workoutId: String) : PlansAction
}