package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.ui.common.UiImage

data class WorkoutMetadataDraft(
    val image: UiImage,
    val name: String,
    val description: String,
    val imageChanged: Boolean = false,
    val isValid: Boolean = false,
    val errors: Map<WorkoutMetadataField, MetadataValidationError> = emptyMap(),
    val touchedFields: Set<WorkoutMetadataField> = emptySet(),
    val creationMode: Boolean = false,
    ) {
    fun displayError(field: WorkoutMetadataField) =
        touchedFields.contains(field) && errors.containsKey(field)
}

enum class WorkoutMetadataField {
    NAME,
    DESCRIPTION,
    IMAGE,
}

sealed interface MetadataValidationError {
    object EmptyName : MetadataValidationError
    object ImageRequired : MetadataValidationError
    object DescriptionTooLong : MetadataValidationError
}

fun WorkoutMetadataDraft.validate(): Map<WorkoutMetadataField, MetadataValidationError> {
    val errors = mutableMapOf<WorkoutMetadataField, MetadataValidationError>()
    if (name.isEmpty()) {
        errors[WorkoutMetadataField.NAME] = MetadataValidationError.EmptyName
    }

    if (description.length > 10_000) {
        errors[WorkoutMetadataField.DESCRIPTION] = MetadataValidationError.DescriptionTooLong
    }

    //na razie wersja gdzie obraz nie jest wymagany
//    if (image.isEmpty()) {
//        errors[WorkoutMetadataField.IMAGE] = MetadataValidationError.ImageRequired
//    }
    return errors
}