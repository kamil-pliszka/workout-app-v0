package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutId

data class WorkoutWithExercises(
    val workout: Workout,
    val exercises: Set<Exercise>,
)

class GetWorkoutWithExercisesUseCase(
    private val repository: WorkoutRepository,
    private val resolveWorkoutExerciseUC: ResolveWorkoutExercisesUseCase,
    private val estimateWorkoutMetricsUC: EstimateWorkoutMetricsUseCase,
) {
    suspend fun execute(workoutId: WorkoutId): WorkoutWithExercises {
        val workout = repository.getWorkout(workoutId)

        val exercises = resolveWorkoutExerciseUC.execute(workout.items)

        //przy pobraniu konkretnego workout przeliczamy metryki
        val metrics = estimateWorkoutMetricsUC.execute(
            workout.items, exercises
        )

        return WorkoutWithExercises(
            workout = workout
                .withMetrics(metrics)
                .withEstimatedKcalForWeight(100.0),
            exercises = exercises
        )
    }
}