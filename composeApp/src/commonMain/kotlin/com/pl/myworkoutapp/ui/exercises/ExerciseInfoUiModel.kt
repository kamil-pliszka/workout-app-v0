package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.UiImage
import com.pl.myworkoutapp.ui.common.UiText


data class ExerciseInfoUiModel(
    val exerciseId: ExerciseId,
    val muscle: MuscleGroup,
    val quantityType: QuantityType,
    val quantityValue: Int,
    val isDirty: Boolean = false,
    val equipment: Equipment,
    val name: UiText,
    val customDesc: UiText?,
    val descExerciseId: BuiltInExerciseId?,//
    val descriptionMarkdown: String?,
    val image: UiImage,
    val position: Int? = null,
    val total: Int? = null,
)
