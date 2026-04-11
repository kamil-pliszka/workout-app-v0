package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel

data class WorkoutDetailsUiState(
    val isLoading: Boolean = false,
    val workout: WorkoutWithExercisesUiModel? = null,
    val isDirty: Boolean = false,
    val selectedItem : WorkoutUiItem? = null,
    val exerciseInfo: ExerciseInfoUiModel? = null,
    val showExercisePicker: Boolean = false,
)
