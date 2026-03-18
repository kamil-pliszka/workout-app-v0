package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId

object LegsWorkouts {
    val LEGS_AND_GLUTES_10_MIN = BuiltInWorkout(
        id = BuiltInWorkoutId.LEGS_AND_GLUTES_10_MIN.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        exercises = listOf(
            BuiltInExerciseId.JUMPING_JACKS.withDuration(20),
        ),
    )

    fun ALL() = listOf(
        LEGS_AND_GLUTES_10_MIN
    )

}