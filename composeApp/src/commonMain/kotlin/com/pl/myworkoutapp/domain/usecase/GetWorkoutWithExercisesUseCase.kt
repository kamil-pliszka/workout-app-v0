package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutHydrator
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.*
import kotlin.math.roundToInt

data class WorkoutWithExercises(
    val workout: Workout,
    val metrics: WorkoutMetrics,
    val exercises: Set<Exercise>,
)

class GetWorkoutWithExercisesUseCase(
    private val hydrator: WorkoutHydrator,
    private val resolveWorkoutExerciseUC: ResolveWorkoutExercisesUseCase,
) {
    suspend fun execute(
        workoutId: WorkoutId,
        weightKg: Double
    ): WorkoutWithExercises {
        val hydrated = hydrator.hydrate(workoutId)
        val exercises = resolveWorkoutExerciseUC.execute(hydrated.items)

        //val weightKg = appSettingRepository.weightFlow.first()
        val metrics = WorkoutMetrics(
            estimatedKcal = (hydrated.baseKcalPerKg * weightKg).roundToInt()
        )

        return WorkoutWithExercises(
            workout = hydrated,
            metrics = metrics,
            exercises = exercises
        )
    }
}