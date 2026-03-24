package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId

sealed interface WorkoutsAction {
//    object OnShowDateDialogAction : WorkoutsAction
//    object OnDialogConfirmAction : WorkoutsAction
    data class OnPageChanged(val index: Int) : WorkoutsAction
    data class NavToExercise(val exerciseId: ExerciseId) : WorkoutsAction
}