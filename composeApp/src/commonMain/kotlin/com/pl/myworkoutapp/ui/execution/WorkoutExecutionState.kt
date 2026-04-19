package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise

sealed interface WorkoutExecutionState {
    //TODO - do konkretnej przebudowy

    data class Running(
        val currentExercise: WorkoutExercise,
        val remainingSeconds: Int
    ) : WorkoutExecutionState

    data class Rest(
        val remainingSeconds: Int
    ) : WorkoutExecutionState

    data class Paused(
        val workoutId: String
    ) : WorkoutExecutionState

    data object Finished : WorkoutExecutionState
}