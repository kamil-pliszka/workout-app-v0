package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession

/**
 * Pełny runtime state engine.
 * Odpowiada za przechowywanie:
 *
 * aktualnego kroku
 *      fazy
 *      timerów
 *      execution plan
 *      referencji do workout/session
 *
 * To jest:
 *      internal engine state
 *      source of truth dla execution flow
 */
data class WorkoutExecutionRuntime(
    val workout: Workout, //to model domenowy - Engine może używać modeli domenowych
    val session: WorkoutSession, //to model domenowy - Engine może używać modeli domenowych

    val steps: List<ExecutionStep>,

    val currentStepIndex: Int,
    val phase: ExecutionPhase,
    val pausedPhase: ExecutionPhase?,

    val remainingSeconds: Int?,
    //val timerState: ExecutionTimerState?
) {
    val currentStep: ExecutionStep?
        get() = steps.getOrNull(currentStepIndex)
}

//TODO - uwzględnić któryś model w przyszłości
sealed interface ExecutionTimerState1 {
    data object None
    data class Countdown(val seconds: Int)
    data class Stopwatch(val elapsed: Int)
}

data class ExecutionTimerState2(
    val remainingMillis: Long,
    val startedAt: Long?,
    val isPaused: Boolean,
)