package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.model.workout.*

sealed interface WorkoutValidationError {
    data object EmptyCircuit : WorkoutValidationError
    data object EmptyWorkout : WorkoutValidationError
}

class ValidateWorkoutUseCase {

    fun execute(workout: CustomWorkout): List<WorkoutValidationError> {
        val errors = mutableListOf<WorkoutValidationError>()

        if (workout.items.isEmpty()) {
            errors += WorkoutValidationError.EmptyWorkout
        }

        workout.items.forEach { item ->
            validateItem(item, errors)
        }

        return errors
    }

    private fun validateItem(
        item: WorkoutItem,
        errors: MutableList<WorkoutValidationError>
    ) {
        when (item) {
            is Circuit -> {
                if (item.items.isEmpty()) {
                    errors += WorkoutValidationError.EmptyCircuit
                }

                // REKURENCJA
                item.items.forEach { child ->
                    validateItem(child, errors)
                }
            }

            is WorkoutExercise -> {
                // tu możesz dodać future rules
            }
        }
    }
}