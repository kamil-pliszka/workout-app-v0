package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import kotlin.time.Clock

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
        runtime: WorkoutExecutionRuntime,
        action: ExecutionAction
    ): WorkoutExecutionRuntime {
        return when (action) {
            ExecutionAction.Tick -> onTick(runtime)

            ExecutionAction.Skip -> onSkip(runtime)

            ExecutionAction.FinishExercise -> onFinishExercise(runtime)

            ExecutionAction.Pause -> onPause(runtime)

            ExecutionAction.Resume -> onResume(runtime)
        }
    }

    // =========================================================
    // Tick
    // =========================================================

    private fun onTick(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {

        return when (val state = runtime.state) {

            // =====================================================
            // INTRO
            // =====================================================

            is EntryState -> {
                if (state.remainingSeconds <= 1) {
                    startWorkout(runtime, state.startStepIndex)
                } else {
                    runtime.copy(
                        state = state.copy(
                            remainingSeconds = state.remainingSeconds - 1
                        )
                    )
                }
            }

            // =====================================================
            // EXERCISE
            // =====================================================

            is ExerciseState -> {
                when (val target = state.targetState) {
                    is ExerciseTargetState.Countdown -> {
                        if (target.remainingSeconds <= 1) {
                            moveNext(runtime)
                        } else {
                            runtime.copy(
                                state = state.copy(
                                    targetState = target.copy(
                                        remainingSeconds =
                                            target.remainingSeconds - 1
                                    )
                                )
                            )
                        }
                    }
                    is ExerciseTargetState.Stopwatch -> {
                        runtime.copy(
                            state = state.copy(
                                targetState = target.copy(
                                    elapsedSeconds =
                                        target.elapsedSeconds + 1
                                )
                            )
                        )
                    }
                    is ExerciseTargetState.Manual ->  {
                        // Tick nic nie robi
                        runtime
                    }
                }
            }
            is RestState -> {
                if (state.remainingSeconds <= 1) {
                    moveNext(runtime)
                } else {
                    runtime.copy(
                        state = state.copy(
                            remainingSeconds =
                                state.remainingSeconds - 1
                        )
                    )
                }
            }
            is PausedState -> {// Tick ignorowany
                runtime
            }
            FinishedState -> {
                runtime
            }
        }
    }

    // =========================================================
    // Actions
    // =========================================================

    private fun onFinishExercise(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        return when (runtime.state) {
            is ExerciseState -> {
                moveNext(runtime)
            }
            else -> {
                runtime
            }
        }
    }

    private fun onSkip(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        return when (runtime.state) {
            is EntryState -> {
                startWorkout(runtime)
            }
            is ExerciseState -> {
                moveNext(runtime)
            }
            is RestState -> {
                moveNext(runtime)
            }
            is PausedState -> {//ignore
                runtime
            }
            FinishedState -> {//ignore
                runtime
            }
        }
    }

    private fun onPause(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        val active = runtime.state as? RunningState ?: return runtime
        return runtime.copy(
            state = PausedState(active)
        )
    }

    private fun onResume(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {
        val paused = runtime.state as? PausedState ?: return runtime
        return runtime.copy(
            state = paused.previous
        )
    }

    // =========================================================
    // Navigation
    // =========================================================

    private fun startWorkout(
        runtime: WorkoutExecutionRuntime,
        startIndex: Int = 0
    ): WorkoutExecutionRuntime {
        val safeStartIndex = startIndex.takeIf { it in runtime.plan.steps.indices } ?: 0
        val step = runtime.plan.steps.getOrNull(safeStartIndex)
        return if (step == null) {
            runtime.copy(
                state = FinishedState
            )
        } else {
            runtime.copy(
                state = createStateForStep(
                    stepIndex = startIndex,
                    step = step
                )
            )
        }
    }

    private fun moveNext(
        runtime: WorkoutExecutionRuntime
    ): WorkoutExecutionRuntime {

        val currentState = runtime.state

        val currentIndex = when (currentState) {
            is ExerciseState -> currentState.stepIndex
            is RestState -> currentState.stepIndex
            is PausedState -> {
                when (val previous = currentState.previous) {
                    is ExerciseState -> previous.stepIndex
                    is RestState -> previous.stepIndex
                    is EntryState -> -1
                }
            }
            is EntryState -> -1
            FinishedState -> {
                return runtime
            }
        }

        val nextIndex = currentIndex + 1

        if (nextIndex >= runtime.plan.steps.size) {

            return runtime.copy(
                state = FinishedState
            )
        }

        val nextStep = runtime.plan.steps[nextIndex]

        return runtime.copy(
            state = createStateForStep(
                stepIndex = nextIndex,
                step = nextStep
            )
        )
    }

    private fun createStateForStep(
        stepIndex: Int,
        step: ExecutionStep
    ): RunningState {
        return when (step) {
            is ExecutionStep.ExerciseStep -> {
                ExerciseState(
                    stepIndex = stepIndex,
                    targetState = createExerciseTargetState(step),
                    startTime = Clock.System.now(),
                )
            }
            is ExecutionStep.RestStep -> {

                RestState(
                    stepIndex = stepIndex,
                    remainingSeconds = step.durationSeconds
                )
            }
        }
    }

    private fun createExerciseTargetState(
        step: ExecutionStep.ExerciseStep
    ): ExerciseTargetState {
        return when (step.quantity.type) {
            QuantityType.DURATION -> {
                ExerciseTargetState.Countdown(
                    remainingSeconds = step.quantity.value
                )
            }
            QuantityType.REPS,
            QuantityType.REPS_PER_SIDE,
            QuantityType.DISTANCE -> {
                ExerciseTargetState.Manual
            }
        }
    }
}