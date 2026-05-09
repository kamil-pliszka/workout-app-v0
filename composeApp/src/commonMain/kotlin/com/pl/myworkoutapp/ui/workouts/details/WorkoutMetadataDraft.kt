package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.ui.common.UiImage

data class WorkoutMetadataDraft(
    val image: UiImage,
    val name: String,
    val description: String,
    val imageChanged: Boolean = false,
    val isValid: Boolean = false,
    val errors: Map<WorkoutMetadataField, String> = emptyMap(),
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

fun WorkoutMetadataDraft.validate(): Map<WorkoutMetadataField, String> {
    val errors = mutableMapOf<WorkoutMetadataField, String>()
    if (name.isEmpty()) {
        errors[WorkoutMetadataField.NAME] = "Name cannot be empty"
    }

    if (description.length > 10_000) {
        errors[WorkoutMetadataField.DESCRIPTION] = "Description too long"
    }

    //obraz nie jest wymagany
//    if (image.isEmpty()) {
//        errors[WorkoutMetadataField.IMAGE] = "Required"
//    }
    return errors
}