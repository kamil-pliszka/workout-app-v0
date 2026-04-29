package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.workout.*

/**
 * Usuwa istneiejący CustomWorkout i zwraca identyfikator workout który został odkryty
 * - czyli zazwyczaj basedOn, lub wcześniejszy
 */
class DeleteCustomWorkoutAndResolveFallbackUseCase(
    private val repository: WorkoutRepository
) {
    suspend fun execute(
        workoutId: WorkoutId,
    ) : WorkoutId? {
        println("Delete workout: $workoutId")

        val workout = repository.getWorkout(workoutId)

        require(workout is CustomWorkout) {
            "Only custom workout can be deleted"
        }

        val basedOn = workout.basedOn

        repository.deleteWorkout(workout.id)

        return basedOn?.let {
            repository.findLatestBasedOn(it)?.id ?: it
        }
    }
}