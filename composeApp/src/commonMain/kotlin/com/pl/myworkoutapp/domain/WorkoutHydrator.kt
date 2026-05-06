package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.domain.usecase.*

class WorkoutHydrator(
    private val repository: WorkoutRepository,
    private val resolveWorkoutExercisesUC: ResolveWorkoutExercisesUseCase,
    private val estimateWorkoutMetricsUC: EstimateWorkoutMetricsUseCase,
) {

    suspend fun hydrate(workoutId: WorkoutId): Workout {
        return when (val workout = repository.getWorkout(workoutId)) {
            is CustomWorkout -> workout // trust persisted metrics

            is BuiltInWorkout -> {
                val exercises = resolveWorkoutExercisesUC.execute(workout.items)
                val metrics = estimateWorkoutMetricsUC.execute(workout.items, exercises)
                workout.copy(
                    estimatedDuration = metrics.durationSeconds,
                    baseKcalPerKg = metrics.baseKcalPerKg
                )
            }
        }
    }
}
