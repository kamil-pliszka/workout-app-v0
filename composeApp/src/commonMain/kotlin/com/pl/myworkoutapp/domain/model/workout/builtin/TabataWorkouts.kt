package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.*

object TabataWorkouts {
    val TABATA_WORKOUT_1 = BuiltInWorkout(
        id = BuiltInWorkoutId.TABATA_1.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            Circuit(
                phase = Phase.MAIN,
                name = "tabata set",
                structure = CircuitStructure.Tabata(
                    rounds = 4,
                    workSec = 20,
                    restSec = 10,
                ),
                items = listOf(
                    BuiltInExerciseId.PLANK.withDuration(20),
                )
            ),
        )
    )

    fun ALL() = listOf(
        TABATA_WORKOUT_1
    )
}
