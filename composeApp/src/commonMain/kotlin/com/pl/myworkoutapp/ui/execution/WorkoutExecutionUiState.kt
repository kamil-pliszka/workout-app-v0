package com.pl.myworkoutapp.ui.execution

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

    data class Intro(
        val title: String?,
        val nextExercise: UiExercise?,
        val progress: Float,
        val remainingSeconds: Int,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Exercise(
        val title: String?,
        val currentExercise: UiExercise,
        val nextExercise: UiExercise?,
        val remainingSeconds: Int?,
        val progress: Float,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Rest(
        val title: String?,
        val nextExercise: UiExercise?,
        val progress: Float,
        val remainingSeconds: Int,
        val canPause: Boolean,
        val canSkip: Boolean,
    ) : WorkoutExecutionUiState

    data class Paused(
        val title: String?,
        val currentExercise: UiExercise?,
        val progress: Float,
    ) : WorkoutExecutionUiState

    data class Finished(
        val title: String?,
    ) : WorkoutExecutionUiState
}