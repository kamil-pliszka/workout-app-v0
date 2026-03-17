package com.pl.myworkoutapp.domain.model.workout

import kotlin.time.Instant

//Wykonanie treningu
data class WorkoutSession(
    val id: Int,
    val workoutId: Int,
    val startTime: Instant,
    val endTime: Instant?,
    val completed: Boolean = false,
    val totalCalories: Double?,
    //val performedExercises: List<PerformedExercise>
)