package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup

data class ExercisePickerUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val allExercises: List<ExercisePickerListItem> = emptyList(),
    val muscleGroups : List<MuscleGroup> = emptyList(),
    val equipments : List<Equipment> = emptyList(),
    val exerciseTypes: List<ExerciseType> = emptyList(),
    val selectedExerciseId: ExerciseId? = null,
    val currentExerciseId: ExerciseId? = null,
    val showExpandedFilters : Boolean = false,
    //val showExercisePreview : Boolean = false,
    val exercisePreview: ExerciseInfoUiModel? = null,
) {
    val isFilterEmpty: Boolean
        get() = query.isBlank() && muscleGroups.isEmpty() && equipments.isEmpty() && exerciseTypes.isEmpty()
}
