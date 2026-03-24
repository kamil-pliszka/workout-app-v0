package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId

object TabataWorkouts {
    val TABATA_WORKOUT_1 = BuiltInWorkout(
        id = BuiltInWorkoutId.TABATA_1.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            Circuit(
                phase = Phase.MAIN,
                name = "tabata set",
                structure = CircuitStructure.Tabata(
                    workSec = 20,
                    restSec = 10,
                ),
                rounds = 8,
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
