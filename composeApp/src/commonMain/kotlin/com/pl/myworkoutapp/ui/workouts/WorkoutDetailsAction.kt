package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId

sealed interface WorkoutDetailsAction {
    object OnStartWorkout : WorkoutDetailsAction
    object OnSaveWorkout : WorkoutDetailsAction
    object OnResetWorkout : WorkoutDetailsAction
    data class ShowExerciseInfo(val exercise: WorkoutUiItem) : WorkoutDetailsAction
    object CloseExerciseInfo : WorkoutDetailsAction
    object ExerciseExchange : WorkoutDetailsAction
    object ExerciseSave : WorkoutDetailsAction
    object ExerciseReset: WorkoutDetailsAction
    object ExercisePrev: WorkoutDetailsAction
    object ExerciseNext: WorkoutDetailsAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutDetailsAction
    data class ExercisePicked(val exerciseId: ExerciseId?) : WorkoutDetailsAction
    object OnBack : WorkoutDetailsAction
    object OnOpenEditor : WorkoutDetailsAction
    object OnCloseEditor: WorkoutDetailsAction
    object OnSaveEditor: WorkoutDetailsAction
    object OnResetEditor : WorkoutDetailsAction
    object OnDeleteRequest: WorkoutDetailsAction
    object OnTuneRequest: WorkoutDetailsAction
}