package com.pl.myworkoutapp.ui.exercises

sealed interface ExerciseEditorAction {
    object OnDismissRequest: ExerciseEditorAction
    object OnDeleteAction: ExerciseEditorAction
    object OnSaveAction: ExerciseEditorAction
    //data class OnPageChanged(val index: Int) : ExerciseEditorAction
    //data class NavToWorkout(val workoutId: WorkoutId) : ExerciseEditorAction
    //data class NavToExercise(val exerciseId: ExerciseId) : ExerciseEditorAction
}