package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_exercise

fun exercise(
    key: Int, depth: Int,
    timeline: List<TimeLineItemType> = listOf()
): ExerciseUiItem {
    return ExerciseUiItem(
        timeline = timeline,
        key = key,
        depth = depth,
        exerciseId = ExerciseId.Custom(key.toLong()),
        quantityType = QuantityType.REPS,
        quantityValue = key,
        name = "EXE:$key".asUiText(),
        icon = Res.drawable.ic_exercise
    )
}

fun circuit(
    key: Int, depth: Int,
    timeline: List<TimeLineItemType> = listOf()
): CircuitUiItem {
    return CircuitUiItem(
        timeline = timeline,
        key = key,
        depth = depth,
        phase = Phase.MAIN,
        structure = CircuitStructure.Standard(key),
        title = "CIRC:$key".asUiText(),
    )
}