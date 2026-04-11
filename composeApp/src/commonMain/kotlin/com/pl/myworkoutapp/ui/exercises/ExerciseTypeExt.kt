package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

fun ExerciseType.asUiText(): UiText {
    return when (this) {
        ExerciseType.CARDIO -> Res.string.exercise_type_cardio.asUiText()
        ExerciseType.STRENGTH -> Res.string.exercise_type_strength.asUiText()
        ExerciseType.MOBILITY -> Res.string.exercise_type_mobility.asUiText()
        ExerciseType.STRETCH -> Res.string.exercise_type_stretch.asUiText()
    }
}


fun ExerciseType.getImageResource(): DrawableResource = when (this) {
    ExerciseType.CARDIO -> Res.drawable.ic_todo //TODO
    ExerciseType.STRENGTH -> Res.drawable.ic_todo //TODO
    ExerciseType.MOBILITY -> Res.drawable.ic_todo //TODO
    ExerciseType.STRETCH -> Res.drawable.ic_todo //TODO
}