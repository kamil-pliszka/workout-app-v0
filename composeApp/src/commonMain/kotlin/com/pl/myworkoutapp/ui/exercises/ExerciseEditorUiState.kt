package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.*
import org.jetbrains.compose.resources.DrawableResource

data class ExerciseEditorUiModel(
    val exerciseId: ExerciseId.Custom = ExerciseId.Custom.NEW,
    val name: String = "",
    val description: String = "",
    val originalImagePath: String? = null,
    val imagePath: String? = null,
    val imageChanged: Boolean = false,
    val imageRes: DrawableResource? = null,
    val basedOn: ExerciseId.BuiltIn? = null,
    val muscle: MuscleGroup? = null,
    val exerciseType: ExerciseType? = null,
    val equipment: Equipment? = null,
    val met: String = "", // string → łatwiejsza edycja
    val quantityType: QuantityType? = null,
)

data class ExerciseEditorUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDirty: Boolean = false,
    val errors: Map<ExeEditorField, String> = emptyMap(),
    val touchedFields: Set<ExeEditorField> = emptySet(),
    val isValid: Boolean = false,
    val exercise: ExerciseEditorUiModel = ExerciseEditorUiModel(),
    val initialExe: ExerciseEditorUiModel = ExerciseEditorUiModel(),
) {
    fun displayError(field: ExeEditorField) =
        touchedFields.contains(field) && errors.containsKey(field)
}

enum class ExeEditorField {
    NAME,
    DESCRIPTION,
    MUSCLE,
    EXERCISE_TYPE,
    EQUIPMENT,
    MET,
    QUANTITY_TYPE,
    IMAGE,
}