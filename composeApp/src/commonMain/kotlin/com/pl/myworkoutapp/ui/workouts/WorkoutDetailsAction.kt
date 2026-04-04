package com.pl.myworkoutapp.ui.workouts

sealed interface WorkoutDetailsAction {
    object OnStartWorkout : WorkoutDetailsAction
    data class ShowExerciseInfo(val exercise: WorkoutUiItem) : WorkoutDetailsAction
    object CloseExerciseInfo : WorkoutDetailsAction
    object ChangeExercise : WorkoutDetailsAction
    object QuantitySave : WorkoutDetailsAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutDetailsAction
//    data class NavToWorkout(val workoutId: WorkoutId) : WorkoutDetailsAction
//    data class NavToExercise(val exerciseId: ExerciseId) : WorkoutDetailsAction
}