package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel

data class WorkoutDetailsUiState(
    val isLoading: Boolean = false,
    val workout: WorkoutWithExercisesUiModel? = null,
    val isDirty: Boolean = false,
    val selectedItem : WorkoutUiItem? = null,//TODO - zmienić na index wybranego elementu
    val exerciseInfo: ExerciseInfoUiModel? = null,
    val showExercisePicker: Boolean = false,
    //val isEditorMode: Boolean = false,
    val editableWorkout: WorkoutWithExercisesUiModel? = null,
)
