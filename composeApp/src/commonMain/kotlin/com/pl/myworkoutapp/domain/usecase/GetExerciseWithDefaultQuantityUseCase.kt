package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise


data class GetExerciseWithDefaultQuantityResult(
    val exercise: Exercise,
    val workoutExercise: WorkoutExercise,
)
class GetExerciseWithDefaultQuantityUseCase(
    private val repository: WorkoutRepository
) {
    suspend fun execute(exerciseId: ExerciseId): GetExerciseWithDefaultQuantityResult {
        val exercise: Exercise = repository.getExercise(exerciseId)
        val workoutExercise = WorkoutExercise(
            exerciseId,
            Quantity(exercise.quantityType, exercise.defaultQuantityValue)
        )
        return GetExerciseWithDefaultQuantityResult(
            exercise = exercise,
            workoutExercise = workoutExercise
        )
    }
}