package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.*

sealed interface ExercisePickerAction {
    data class Search(val q: String) : ExercisePickerAction
    object ClearFilters : ExercisePickerAction
    object ExpandFilters : ExercisePickerAction
    object CloseFilters : ExercisePickerAction
    data class AddMuscleFilter(val muscle: MuscleGroup) : ExercisePickerAction
    data class AddEquipmentFilter(val equipment: Equipment) : ExercisePickerAction
    data class AddExerciseTypeFilter(val type: ExerciseType) : ExercisePickerAction
    data class RemoveMuscleFilter(val muscle: MuscleGroup) : ExercisePickerAction
    data class RemoveEquipmentFilter(val equipment: Equipment) : ExercisePickerAction
    data class RemoveExerciseTypeFilter(val type: ExerciseType) : ExercisePickerAction
    data class SetFilters(val muscles: List<MuscleGroup>, val equipments: List<Equipment>, val types: List<ExerciseType>) : ExercisePickerAction
    data class ExerciseSelectionToggle(val exerciseId: ExerciseId) : ExercisePickerAction
    data class ExercisePreview(val exerciseId: ExerciseId) : ExercisePickerAction
    object ExercisePreviewClose : ExercisePickerAction
}