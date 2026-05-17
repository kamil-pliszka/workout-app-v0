package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.ui.execution.countdownDurationSeconds

/**
 * Czysta logika state transition.
 * Odpowiada za:
 *
 * state + action -> newState
 * przejścia faz
 * logikę ticków
 * moveNext
 * pause/resume
 *
 * Powinien być:
 * pure / deterministic / bez coroutine / bez side effectów
 */
class ExecutionReducer {

    fun reduce(
        state: WorkoutExecutionRuntime,
        action: ExecutionAction
    ): WorkoutExecutionRuntime {
        return when (action) {
            ExecutionAction.Tick -> onTick(state)

            ExecutionAction.Skip -> onSkip(state)

            ExecutionAction.FinishExercise -> moveNext(state)

            ExecutionAction.Pause -> onPause(state)

            ExecutionAction.Resume -> restorePhase(state)
        }
    }

    /*private fun start(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {

        if (state.steps.isEmpty()) {
            return state.copy(
                phase = ExecutionPhase.Finished
            )
        }

        val first = state.steps.first()

        return when (first) {

            is ExecutionStep.Exercise -> {
                state.copy(
                    currentStepIndex = 0,
                    phase = ExecutionPhase.Exercise,
                    remainingSeconds = null,
                )
            }

            is ExecutionStep.Rest -> {
                state.copy(
                    currentStepIndex = 0,
                    phase = ExecutionPhase.Rest,
                    remainingSeconds = first.durationSeconds,
                )
            }
        }
    }*/

    private fun onTick(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        if (state.phase == ExecutionPhase.Paused) {
            return state
        }

        val remaining = state.remainingSeconds ?: return state

        if (remaining <= 1) {
            return when (state.phase) {
                ExecutionPhase.Intro ->
                    startFirstStep(state)

                else ->
                    moveNext(state)
            }
        }

        return state.copy(
            remainingSeconds = remaining - 1
        )
    }

    private fun startFirstStep(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        val first = state.steps.firstOrNull()
            ?: return state.copy(
                phase = ExecutionPhase.Finished
            )
        return when (first) {
            is ExecutionStep.ExerciseStep -> {
                state.copy(
                    currentStepIndex = 0,
                    phase = ExecutionPhase.Exercise,
                    remainingSeconds = first.countdownDurationSeconds(),
                )
            }

            is ExecutionStep.RestStep -> {
                state.copy(
                    currentStepIndex = 0,
                    phase = ExecutionPhase.Rest,
                    remainingSeconds = first.durationSeconds,
                )
            }
        }
    }

    private fun moveNext(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {

        val nextIndex = state.currentStepIndex + 1

        if (nextIndex >= state.steps.size) {
            return state.copy(
                phase = ExecutionPhase.Finished,
                remainingSeconds = null,
            )
        }

        val nextStep = state.steps[nextIndex]

        return when (nextStep) {

            is ExecutionStep.ExerciseStep -> {
                state.copy(
                    currentStepIndex = nextIndex,
                    phase = ExecutionPhase.Exercise,
                    remainingSeconds = nextStep.countdownDurationSeconds()
                )
            }

            is ExecutionStep.RestStep -> {
                state.copy(
                    currentStepIndex = nextIndex,
                    phase = ExecutionPhase.Rest,
                    remainingSeconds = nextStep.durationSeconds,
                )
            }
        }
    }

    private fun restorePhase(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {

        val current = state.steps[state.currentStepIndex]

        return when (current) {

            is ExecutionStep.ExerciseStep -> {
                state.copy(
                    phase = state.pausedPhase ?: ExecutionPhase.Exercise,
                    pausedPhase = null
                )
            }

            is ExecutionStep.RestStep -> {
                state.copy(
                    phase = ExecutionPhase.Rest
                )
            }
        }
    }

    private fun onSkip(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        return when (state.phase) {
            ExecutionPhase.Intro ->
                startFirstStep(state)
            else ->
                moveNext(state)
        }
    }


    private fun onPause(
        state: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        return state.copy(
            pausedPhase = state.phase,
            phase = ExecutionPhase.Paused
        )
    }
}