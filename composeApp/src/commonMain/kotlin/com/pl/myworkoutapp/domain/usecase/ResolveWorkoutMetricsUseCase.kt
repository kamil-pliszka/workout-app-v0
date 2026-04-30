package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout

class ResolveWorkoutMetricsUseCase(
    private val estimateWorkoutMetricsUseCase: EstimateWorkoutMetricsUseCase
) {
    fun execute(workout: BuiltInWorkout, exercises: Set<Exercise>): BuiltInWorkout {
        if (workout.estimatedDuration != null && workout.baseKcalPerKg != null) {
            return workout
        }

        val metrics = estimateWorkoutMetricsUseCase.execute(workout.items, exercises)

        return workout.copy(
            estimatedDuration = metrics.durationSeconds,
            baseKcalPerKg = metrics.baseKcalPerKg
        )
    }

    fun execute(workout: CustomWorkout, exercises: Set<Exercise>): CustomWorkout {
        if (workout.estimatedDuration != null && workout.baseKcalPerKg != null) {
            return workout
        }

        val metrics = estimateWorkoutMetricsUseCase.execute(workout.items, exercises)

        return workout.copy(
            estimatedDuration = metrics.durationSeconds,
            baseKcalPerKg = metrics.baseKcalPerKg
        )
    }
}