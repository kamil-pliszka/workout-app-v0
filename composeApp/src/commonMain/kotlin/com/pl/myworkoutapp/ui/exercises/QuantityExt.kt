package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.exercise_qty_distance
import myworkoutapplication.composeapp.generated.resources.exercise_qty_duration
import myworkoutapplication.composeapp.generated.resources.exercise_qty_reps
import myworkoutapplication.composeapp.generated.resources.exercise_qty_reps_per_side
import myworkoutapplication.composeapp.generated.resources.qty_value_reps
import myworkoutapplication.composeapp.generated.resources.qty_value_reps_per_side

private fun Int.pad2(): String = this.toString().padStart(2, '0')
private fun Int.pad3(): String = this.toString().padStart(3, '0')

fun Int.secondsAsText(): String {
    return when {
        this < 0 -> "?"
        this == 0 -> "0"
        this < 60 -> this.toString() + "s"
        else -> {
            val minutes = this / 60
            val seconds = this % 60
            "${minutes.pad2()}:${seconds.pad2()}"
        }
    }
}

fun Int.distanceAsText(): String {
    return when {
        this < 0 -> "?"
        this == 0 -> "0"
        this < 1000 -> this.toString() + "m"
        else -> {
            val km = this / 1000
            val m = this % 1000
            "${km}.${m.pad3()} km"
        }
    }
}

fun QuantityType.asUiText(): UiText {
    return when (this) {
        QuantityType.REPS -> Res.string.exercise_qty_reps.asUiText()
        QuantityType.REPS_PER_SIDE -> Res.string.exercise_qty_reps_per_side.asUiText()
        QuantityType.DURATION -> Res.string.exercise_qty_duration.asUiText()
        QuantityType.DISTANCE -> Res.string.exercise_qty_distance.asUiText()
    }
}

fun Int?.qtyValueAsUiText(qType: QuantityType): UiText =
    if (this == null)
        EmptyUiText
    else
        when (qType) {
            QuantityType.REPS -> Res.string.qty_value_reps.asUiText(this)
            QuantityType.REPS_PER_SIDE -> Res.string.qty_value_reps_per_side.asUiText(this)
            QuantityType.DURATION -> this.secondsAsText().asUiText()
            QuantityType.DISTANCE -> this.distanceAsText().asUiText()
        }
