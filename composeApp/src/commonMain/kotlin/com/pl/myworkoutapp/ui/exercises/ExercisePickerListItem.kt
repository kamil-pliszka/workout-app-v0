package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.UiImage

data class ExercisePickerListItem(
    val exerciseId: ExerciseId,
    val searchKey: String,
    val name: String,
    val muscle: MuscleGroup,
    val equipment: Equipment,
    val exerciseType: ExerciseType,
    val image: UiImage,
)