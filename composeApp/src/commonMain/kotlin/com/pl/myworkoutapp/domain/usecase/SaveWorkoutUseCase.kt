package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.workout.*

class SaveWorkoutUseCase(
    private val repository: WorkoutRepository
) {
    suspend fun execute(
        workoutId: WorkoutId,
        basedOn: WorkoutId.BuiltIn?,
        difficulty: Difficulty,
        items: List<WorkoutItem>
    ): WorkoutId {
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
        val customWorkout = CustomWorkout(
            id = customId,
            name = null,//TODO
            description = null,//TODO
            imageUri = null,//TODO
            basedOn = finalBasedOn,
            difficulty = difficulty,
            items = items
        )

        return repository.saveCustomWorkout(customWorkout)
    }
}