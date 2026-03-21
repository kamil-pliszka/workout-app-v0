package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import kotlin.time.Instant

//Wykonanie treningu
data class WorkoutSession(
    val id: Int,
    val workoutId: WorkoutId,
    val startTime: Instant,
    val endTime: Instant?,
    val completed: Boolean = false,
    val totalCalories: Double?,
    val performedExercises: List<PerformedExercise>
) {
    init {
        if (completed) require(endTime != null)
        if (endTime != null) require(endTime >= startTime)
    }
}


data class PerformedExercise(
    val exerciseId: ExerciseId,
    val plannedQuantity: Quantity,
    val actualQuantity: Quantity?,
    val startTime: Instant,
    val endTime: Instant?,
)