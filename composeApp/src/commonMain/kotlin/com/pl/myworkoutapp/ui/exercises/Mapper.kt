package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.CustomExercise
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.toBuiltInExerciseId
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.common.toUiConfig

fun Exercise.toUi(): ExerciseInfoUiModel = when(this) {
    is BuiltInExercise -> {
        val config = id.toBuiltInExerciseId().toUiConfig()
        ExerciseInfoUiModel(
            exerciseId = id,
            muscle = muscle,
            quantityType = quantityType,
            name = config.name,
            customDesc = null,
            descExerciseId = id.toBuiltInExerciseId(),
            descriptionMarkdown = null, //będzie ustawione w VM
            icon = config.image,
            imagePath = null,
            equipment = equipment,
        )
    }
    is CustomExercise -> {
        val configBase = basedOn?.toBuiltInExerciseId()?.toUiConfig()
        ExerciseInfoUiModel(
            exerciseId = id,
            muscle = muscle,
            quantityType = quantityType,
            name = when {
                name.isNotBlank() -> name.asUiText()
                configBase != null -> configBase.name
                else -> EmptyUiText
            },
            customDesc = when {
                !description.isNullOrBlank() -> description.asUiText()
                else -> null
            },
            descExerciseId = basedOn?.toBuiltInExerciseId(),
            descriptionMarkdown = null, //będzie ustawione w VM
            icon = when {
                !imageUri.isNullOrEmpty() -> null
                configBase != null -> configBase.image
                else -> null
            },
            imagePath = when {
                !imageUri.isNullOrEmpty() -> imageUri
                else -> null
            },
            equipment = equipment,
        )
    }
}