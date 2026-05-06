package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.workout.*

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
        workout : CustomWorkout
    ): SaveWorkoutResult {
        workout.items.forEachIndexed { index, item ->
            println("ITEM[$index]: $item")
        }

        val validationErrors = validateWorkoutUseCase.execute(workout)
        if (validationErrors.isNotEmpty()) {
            return SaveWorkoutResult.ValidationError(validationErrors)
        }

//        val finalBasedOn: WorkoutId.BuiltIn? = when (workoutId) {
//            is WorkoutId.BuiltIn -> workoutId
//            is WorkoutId.Custom -> basedOn
//        }
//        val customId: WorkoutId.Custom = when (workoutId) {
//            is WorkoutId.BuiltIn -> WorkoutId.Custom.NEW
//            is WorkoutId.Custom -> workoutId
//        }

        val exercises = resolveWorkoutExercisesUC.execute(workout.items)
        val metrics = estimateWorkoutMetricsUC.execute(workout.items, exercises)

//        val customWorkout = CustomWorkout(
//            id = customId,
//            name = name,
//            description = description,
//            imageUri = imageUri,
//            basedOn = finalBasedOn,
//            difficulty = difficulty,
//            estimatedDuration = metrics.durationSeconds,
//            baseKcalPerKg = metrics.baseKcalPerKg,
//            items = items,
//        )
        val customWorkout = workout.copy(
            estimatedDuration = metrics.durationSeconds,
            baseKcalPerKg = metrics.baseKcalPerKg,
        )

        val savedId = repository.saveCustomWorkout(customWorkout)
        return SaveWorkoutResult.Success(savedId)
    }
}