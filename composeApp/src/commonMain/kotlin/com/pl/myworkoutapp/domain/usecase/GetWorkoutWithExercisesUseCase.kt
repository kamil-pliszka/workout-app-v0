package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.*

data class WorkoutWithExercises(
    val workout : Workout,
    val exercises: Set<Exercise>,
)
class GetWorkoutWithExercisesUseCase(
    private val repository: WorkoutRepository
) {
    suspend fun execute(workoutId: WorkoutId): WorkoutWithExercises {
        val workout = repository.getWorkout(workoutId)
        // 1. Identify unique exercise IDs
        val exerciseIds = workout.items.extractExerciseIds()

        // 2. Fetch all required exercises in parallel
        val exercises = coroutineScope {
            exerciseIds.map { id ->
                async { repository.getExercise(id) }
            }.awaitAll().toSet()
        }

        return WorkoutWithExercises(
            workout = workout,
            exercises = exercises
        )
    }
}