package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise

sealed interface WorkoutExecutionState {

    data class Running(
        val currentExercise: WorkoutExercise,
        val remainingSeconds: Int
    ) : WorkoutExecutionState

    data class Rest(
        val remainingSeconds: Int
    ) : WorkoutExecutionState

    data object Paused : WorkoutExecutionState

    data object Finished : WorkoutExecutionState
}