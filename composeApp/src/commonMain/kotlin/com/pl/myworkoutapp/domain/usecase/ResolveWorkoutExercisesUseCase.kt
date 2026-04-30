package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem
import com.pl.myworkoutapp.domain.model.workout.extractExerciseIds
import kotlinx.coroutines.*

class ResolveWorkoutExercisesUseCase(
    private val repository: WorkoutRepository,
) {
    suspend fun execute(
        items: List<WorkoutItem>
    ): Set<Exercise> {
        // 1. Identify unique exercise IDs
        val exerciseIds = items.extractExerciseIds()
        // 2. Fetch all required exercises in parallel
        val exercises = coroutineScope {
            exerciseIds.map { id ->
                async { repository.getExercise(id) }
            }.awaitAll().toSet()
        }
        return exercises
    }
}