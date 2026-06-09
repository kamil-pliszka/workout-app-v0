package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.ui.common.UiText

/**
 * Stabilny, płaski model dla Compose.
 * Odpowiada za:
 * dane potrzebne do renderowania UI
 * Powinien:
 * być prosty
 * immutable
 * pozbawiony logiki biznesowej
 */
sealed interface WorkoutExecutionUiState {
    data object Loading : WorkoutExecutionUiState

    data class Entry(
        val title: UiText,
        val nextExercise: UiExercise?,
        val progress: Float,
        val remainingSeconds: Int,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Exercise(
        val title: UiText,
        val currentExercise: UiExercise,
        val nextExercise: UiExercise?,
        //val remainingSeconds: Int?,
        val target: UiExerciseTarget,
        val progress: Float,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Rest(
        val title: UiText,
        val nextExercise: UiExercise?,
        val progress: Float,
        val remainingSeconds: Int,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Paused(
        val title: UiText,
        val currentExercise: UiExercise?,
        val progress: Float,
    ) : WorkoutExecutionUiState

    data class Finished(
        val title: UiText,
    ) : WorkoutExecutionUiState
}

sealed interface UiExerciseTarget {
    data class Duration(
        val remainingSeconds: Int
    ) : UiExerciseTarget
    data class Reps(
        val reps: Int
    ) : UiExerciseTarget
    data class Distance(
        val meters: Int
    ) : UiExerciseTarget
}