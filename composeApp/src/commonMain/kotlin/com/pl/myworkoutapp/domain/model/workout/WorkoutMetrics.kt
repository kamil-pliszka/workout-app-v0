package com.pl.myworkoutapp.domain.model.workout

/**
 * Metryki workout, uwzględniające dane usera
 */
data class WorkoutMetrics(
    val estimatedKcal: Int
)

data class WorkoutWithMetrics(
    val workout: Workout,
    val metrics: WorkoutMetrics
)