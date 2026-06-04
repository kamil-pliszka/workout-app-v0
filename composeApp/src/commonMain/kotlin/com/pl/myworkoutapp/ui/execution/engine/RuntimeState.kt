package com.pl.myworkoutapp.ui.execution.engine

import kotlin.time.Instant

sealed interface RuntimeState
sealed interface RunningState : RuntimeState

data class IntroState(
    val remainingSeconds: Int
) : RunningState

data class ExerciseState(
    val stepIndex: Int,
    val targetState: ExerciseTargetState,
    val startTime: Instant,
) : RunningState

data class RestState(
    val stepIndex: Int,
    val remainingSeconds: Int
) : RunningState

data class PausedState(
    val previous: RunningState
) : RuntimeState

data object FinishedState : RuntimeState

fun RuntimeState.activeStateOrNull(): RunningState? {
    return when(this) {
        is RunningState -> this
        is PausedState -> previous
        FinishedState -> null
    }
}

fun RuntimeState.stepIndexOrNull(): Int? {
    return when(val active = activeStateOrNull()) {
        is ExerciseState -> active.stepIndex
        is RestState -> active.stepIndex
        is IntroState -> null
        null -> null
    }
}
