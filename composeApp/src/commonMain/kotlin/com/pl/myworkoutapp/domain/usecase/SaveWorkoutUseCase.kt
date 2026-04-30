package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.workout.*
import kotlin.math.roundToInt

sealed interface SaveWorkoutResult {
    data class Success(val workoutId: WorkoutId) : SaveWorkoutResult
    data class ValidationError(val errors: List<WorkoutValidationError>) : SaveWorkoutResult
}

class SaveWorkoutUseCase(
    private val repository: WorkoutRepository,
    private val validateWorkoutUseCase: ValidateWorkoutUseCase,
    private val resolveWorkoutExercisesUC: ResolveWorkoutExercisesUseCase,
    private val estimateWorkoutMetricsUC: EstimateWorkoutMetricsUseCase
) {
    suspend fun execute(
        workoutId: WorkoutId,
        basedOn: WorkoutId.BuiltIn?,
        difficulty: Difficulty,
        items: List<WorkoutItem>,
        weightKg : Double = 100.0, //TODO - na razie zahardkodowana wartość
    ): SaveWorkoutResult {
        println("Saving workout: $workoutId")
        items.forEachIndexed { index, item ->
            println("ITEM[$index]: $item")
        }
        val finalBasedOn: WorkoutId.BuiltIn? = when (workoutId) {
            is WorkoutId.BuiltIn -> workoutId
            is WorkoutId.Custom -> basedOn
        }
        val customId: WorkoutId.Custom = when (workoutId) {
            is WorkoutId.BuiltIn -> WorkoutId.Custom.NEW
            is WorkoutId.Custom -> workoutId
        }

        val exercises = resolveWorkoutExercisesUC.execute(items)
        val metrics = estimateWorkoutMetricsUC.execute(items, exercises)
        val estimatedKcal = metrics.baseKcalPerKg * weightKg

        val customWorkout = CustomWorkout(
            id = customId,
            name = null,//TODO
            description = null,//TODO
            imageUri = null,//TODO
            basedOn = finalBasedOn,
            difficulty = difficulty,
            estimatedDuration = metrics.durationSeconds,
            baseKcalPerKg = metrics.baseKcalPerKg,
            estimatedKcal = estimatedKcal.roundToInt(),
            items = items,
        )

        val validationErrors = validateWorkoutUseCase.execute(customWorkout)

        if (validationErrors.isNotEmpty()) {
            return SaveWorkoutResult.ValidationError(validationErrors)
        }

        val savedId = repository.saveCustomWorkout(customWorkout)
        return SaveWorkoutResult.Success(savedId)
    }
}