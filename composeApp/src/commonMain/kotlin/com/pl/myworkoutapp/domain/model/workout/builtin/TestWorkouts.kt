package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.*

object TestWorkouts {
    val W_1_EXE_DURATION = BuiltInWorkout(
        id = BuiltInWorkoutId.W_1_EXE_DURATION.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.PLANK.withDuration(20),
        )
    )

    val W_1_EXE_REPS = BuiltInWorkout(
        id = BuiltInWorkoutId.W_1_EXE_REPS.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.PUSH_UP.withReps(20),
        )
    )

    val W_1_EXE_DISTANCE = BuiltInWorkout(
        id = BuiltInWorkoutId.W_1_EXE_DISTANCE.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.RUNNING.withDistance(1200),
        )
    )


    val W_2_EXE = BuiltInWorkout(
        id = BuiltInWorkoutId.W_2_EXE.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.PLANK.withDuration(20),
            BuiltInExerciseId.PUSH_UP.withReps(20),
        )
    )


    fun ALL() = listOf(
        W_1_EXE_DURATION,
        W_1_EXE_REPS,
        W_1_EXE_DISTANCE,
        W_2_EXE
    )
}
