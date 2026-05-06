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
            quantityValue = defaultQuantityValue,
            name = config.name.asUiText(),
            customDesc = null,
            descExerciseId = id.toBuiltInExerciseId(),
            descriptionMarkdown = null, //będzie ustawione w VM
            image = config.image.asUiImage(),
            equipment = equipment,
        )
    }

    is CustomExercise -> {
        val configBase = basedOn?.toBuiltInExerciseId()?.toUiConfig()
        ExerciseInfoUiModel(
            exerciseId = id,
            muscle = muscle,
            quantityType = quantityType,
            quantityValue = defaultQuantityValue,
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
            image = when {
                !imageUri.isNullOrEmpty() -> imageUri.asUiImage()
                configBase != null -> configBase.image.asUiImage()
                else -> UiImage.Empty
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
            image = config.image.asUiImage(),
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
            image = when {
                !imageUri.isNullOrEmpty() -> imageUri.asUiImage()
                configBase != null -> configBase.image.asUiImage()
                else -> UiImage.Empty
            }
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
        quantityType = quantityType,
        defaultQuantityValue = defaultQuantityValue.toString(),
        secondsPerRep = secondsPerRep?.toString() ?: "",
        metersPerSecond = metersPerSecond?.toString() ?: "",
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
        quantityType = quantityType,
        defaultQuantityValue = defaultQuantityValue.toString(),
        secondsPerRep = secondsPerRep?.toString() ?: "",
        metersPerSecond = metersPerSecond?.toString() ?: "",
    )
}

fun ExerciseEditorUiModel.toDomain(): CustomExercise {
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
        defaultQuantityValue = defaultQuantityValue.toInt(),
        secondsPerRep = secondsPerRep.toDoubleOrNull(),
        metersPerSecond = metersPerSecond.toDoubleOrNull(),
    )
}
