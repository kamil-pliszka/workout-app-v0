package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.model.workout.*

sealed interface WorkoutValidationError {
    data object EmptyCircuit : WorkoutValidationError
    data object EmptyWorkout : WorkoutValidationError
    data object EmptyName : WorkoutValidationError
    data object EmptyDescription : WorkoutValidationError
    data object EmptyImage : WorkoutValidationError
}

class ValidateWorkoutUseCase {

    fun execute(workout: CustomWorkout): List<WorkoutValidationError> {
        val errors = mutableListOf<WorkoutValidationError>()

        if (workout.name.isNullOrEmpty()) {
            errors += WorkoutValidationError.EmptyName
        }
        if (workout.description.isNullOrEmpty() && workout.basedOn == null) {
            //opis wymagany tylko dla w pełni customowego workoutu
            errors += WorkoutValidationError.EmptyDescription
        }
        if (workout.imageUri.isNullOrEmpty() && workout.basedOn == null) {
            //obrazek wymagany tylko dla w pełni customowego workoutu
            errors += WorkoutValidationError.EmptyImage
        }

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