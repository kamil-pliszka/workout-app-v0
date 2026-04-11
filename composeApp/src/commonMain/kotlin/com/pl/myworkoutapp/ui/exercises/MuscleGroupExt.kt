package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

fun MuscleGroup.asUiText(): UiText {
    return when (this) {
        MuscleGroup.ABS -> Res.string.muscle_group_abs.asUiText()
        MuscleGroup.BACK -> Res.string.muscle_group_back.asUiText()
        MuscleGroup.CHEST -> Res.string.muscle_group_chest.asUiText()
        MuscleGroup.ARMS -> Res.string.muscle_group_arms.asUiText()
        MuscleGroup.LEGS -> Res.string.muscle_group_legs.asUiText()
        MuscleGroup.CORE -> Res.string.muscle_group_core.asUiText()
    }
}

fun MuscleGroup.getImageResource(): DrawableResource = when (this) {
    MuscleGroup.ABS -> Res.drawable.ic_muscle_group_abs
    MuscleGroup.BACK -> Res.drawable.ic_todo//TODO
    MuscleGroup.CHEST -> Res.drawable.ic_muscle_group_chest
    MuscleGroup.ARMS -> Res.drawable.ic_todo//TODO
    MuscleGroup.LEGS -> Res.drawable.ic_todo//TODO
    MuscleGroup.CORE -> Res.drawable.ic_muscle_group_core
}