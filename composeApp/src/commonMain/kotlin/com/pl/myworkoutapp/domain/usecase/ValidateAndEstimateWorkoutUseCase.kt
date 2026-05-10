package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import kotlin.math.roundToInt

sealed interface SaveWorkoutEditorResult {
    data class Error(val errors: List<WorkoutValidationError>): SaveWorkoutEditorResult
    data class Success(
        val estimatedDuration : Int,
        val estimatedKcal : Int,
        ): SaveWorkoutEditorResult
}

class ValidateAndEstimateWorkoutUseCase(
    private val validateWorkout: ValidateWorkoutUseCase,
    private val metricsUseCase: EstimateWorkoutMetricsUseCase,
    private val resolveWorkoutExercisesUC: ResolveWorkoutExercisesUseCase,
) {

    suspend fun execute(
        workout : CustomWorkout,
        weightKg: Double
    ): SaveWorkoutEditorResult {
        val errors = validateWorkout.execute(workout)

        if (errors.isNotEmpty()) {
            return SaveWorkoutEditorResult.Error(errors)
        }

        val exercises = resolveWorkoutExercisesUC.execute(workout.items)
        val metrics = metricsUseCase.execute(
            workout.items,
            exercises
        )

        val estimatedKcal = (metrics.baseKcalPerKg * weightKg).roundToInt()

        return SaveWorkoutEditorResult.Success(
            estimatedDuration = metrics.durationSeconds,
            estimatedKcal = estimatedKcal
        )
    }
}