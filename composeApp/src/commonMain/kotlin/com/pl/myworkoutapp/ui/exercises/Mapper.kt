package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.*
import org.jetbrains.compose.resources.getString

fun Exercise.toUi(): ExerciseInfoUiModel = when (this) {
    is BuiltInExercise -> {
        val config = id.toBuiltInExerciseId().toUiConfig()
        ExerciseInfoUiModel(
            exerciseId = id,
            muscle = muscle,
            quantityType = quantityType,
            name = config.name.asUiText(),
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
                configBase != null -> configBase.name.asUiText()
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

suspend fun Exercise.toSearchUi(): ExercisePickerListItem = when (this) {
    is BuiltInExercise -> {
        val config = id.toBuiltInExerciseId().toUiConfig()
        ExercisePickerListItem(
            exerciseId = id,
            name = getString(config.name),
            searchKey = id.toBuiltInExerciseId().name,
            muscle = muscle,
            equipment = equipment,
            exerciseType = exerciseType,
            icon = config.image,
            imagePath = null,
        )
    }

    is CustomExercise -> {
        val configBase = basedOn?.toBuiltInExerciseId()?.toUiConfig()
        ExercisePickerListItem(
            exerciseId = id,
            name = when {
                name.isNotBlank() -> name
                configBase != null -> getString(configBase.name)
                else -> ""
            },
            searchKey = basedOn?.toBuiltInExerciseId()?.name ?: "",
            muscle = muscle,
            equipment = equipment,
            exerciseType = exerciseType,
            icon = when {
                !imageUri.isNullOrEmpty() -> null
                configBase != null -> configBase.image
                else -> null
            },
            imagePath = when {
                !imageUri.isNullOrEmpty() -> imageUri
                else -> null
            },
        )
    }
}

fun CustomExercise.toExerciseEditorUiModel(): ExerciseEditorUiModel {
    val configBase = basedOn?.toBuiltInExerciseId()?.toUiConfig()
    return ExerciseEditorUiModel(
        exerciseId = id,
        name = name,
        description = description ?: "",
        originalImagePath = imageUri,
        imagePath = imageUri,
        imageRes = configBase?.image,
        basedOn = basedOn,
        muscle = muscle,
        exerciseType = exerciseType,
        equipment = equipment,
        met = met.toString(),
        quantityType = quantityType
    )
}


fun BuiltInExercise.toExerciseEditorUiModel(): ExerciseEditorUiModel {
    val configBase = id.toBuiltInExerciseId().toUiConfig()
    return ExerciseEditorUiModel(
        //exerciseId = id,
        //name = name,
        //description = description ?: "",
        //originalImagePath = imageUri,
        //imagePath = imageUri,
        imageRes = configBase.image,
        basedOn = this.id,
        muscle = muscle,
        exerciseType = exerciseType,
        equipment = equipment,
        met = met.toString(),
        quantityType = quantityType
    )
}

fun ExerciseEditorUiModel.toDomain() : CustomExercise {
    return CustomExercise(
        id = exerciseId,
        name = name,
        description = description,
        imageUri = imagePath,
        basedOn = basedOn,
        muscle = requireNotNull(muscle),
        exerciseType = requireNotNull(exerciseType),
        equipment = requireNotNull(equipment),
        met = met.toDouble(),
        quantityType = requireNotNull(quantityType),
    )
}
