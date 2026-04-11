package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import org.jetbrains.compose.resources.DrawableResource

data class ExercisePickerListItem(
    val exerciseId: ExerciseId,
    val searchKey: String,
    val name: String,
    val muscle: MuscleGroup,
    val equipment: Equipment,
    val exerciseType: ExerciseType,
    val icon: DrawableResource?,
    val imagePath: String?,
)