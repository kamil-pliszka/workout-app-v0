package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.workout.WorkoutId

sealed interface WorkoutsAction {
    data class OnPageChanged(val index: Int) : WorkoutsAction
    data object AddExercise: WorkoutsAction
    data object AddWorkout: WorkoutsAction
    data class ShowWorkoutDetails(val workoutId: WorkoutId) : WorkoutsAction
}