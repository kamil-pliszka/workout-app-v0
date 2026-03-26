package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.ui.common.UiText
import org.jetbrains.compose.resources.DrawableResource


data class ExerciseInfoUiModel(
    val exerciseId: ExerciseId,
    val muscle: MuscleGroup,
    val quantityType: QuantityType,
    val quantityValue: Int? = null,
    val equipment: Equipment,
    val name: UiText,
    val customDesc: UiText?,
    val descExerciseId: BuiltInExerciseId?,//
    val icon: DrawableResource
)