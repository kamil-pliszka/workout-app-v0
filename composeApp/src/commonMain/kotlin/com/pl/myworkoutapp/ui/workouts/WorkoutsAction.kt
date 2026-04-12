package com.pl.myworkoutapp.ui.workouts

sealed interface WorkoutsAction {
//    object OnShowDateDialogAction : WorkoutsAction
//    object OnDialogConfirmAction : WorkoutsAction
    data class OnPageChanged(val index: Int) : WorkoutsAction
}