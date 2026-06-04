package com.pl.myworkoutapp.ui.execution.engine

sealed interface ExerciseTargetState {
    data class Countdown(
        val remainingSeconds: Int
    ) : ExerciseTargetState

    data class Stopwatch(
        val elapsedSeconds: Int
    ) : ExerciseTargetState

    data object Manual : ExerciseTargetState
}