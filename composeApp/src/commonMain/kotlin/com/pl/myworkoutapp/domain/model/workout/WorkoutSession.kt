package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import com.pl.myworkoutapp.domain.model.plan.PlanId
import kotlin.time.Instant

//Wykonanie treningu
data class WorkoutSession(
    val id: Long,
    val planId: PlanId?,
    val workoutId: WorkoutId,
    val startTime: Instant,
    val endTime: Instant?,
    val progress: Float,
    val completed: Boolean = false,
    val estimatedDuration: Int,//in seconds
    val estimatedCalories: Int,//total, in kcal
    val executionTime: Double, //czas samych ćwiczeń(bez przerw), aktualizowane po każdym ćwiczeniu
    val calories: Double, //spalone kalorie, aktualizowane po każdym ćwiczeniu
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