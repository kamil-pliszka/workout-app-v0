package com.pl.myworkoutapp.ui.execution.engine

import kotlin.time.Clock

class ExecutionEventResolver {
    fun resolve(
        old: WorkoutExecutionRuntime,
        new: WorkoutExecutionRuntime,
        action: ExecutionAction
    ): List<ExecutionEvent> {
        if (action is ExecutionAction.Pause || action is ExecutionAction.Resume) {
            return emptyList()
        }
        return buildList {
            val oldStepIndex = old.state.stepIndexOrNull()
            val newStepIndex = new.state.stepIndexOrNull()
            val now = Clock.System.now()
            if (old.state is ExerciseState) {
                //zakończenie ćwiczenia
                if (oldStepIndex != null && oldStepIndex != newStepIndex) {
                    val exeStep =
                        old.plan.steps.getOrNull(oldStepIndex) as? ExecutionStep.ExerciseStep
                    exeStep?.let {
                        add(
                            ExecutionEvent.ExerciseCompleted(
                                weightKg = old.weightKg,
                                sessionId = old.session.id,
                                exercise = exeStep.exercise,
                                quantity = exeStep.quantity,
                                startTime = old.state.startTime,
                                endTime = now,
                                progress = new.calculateProgress()
                            )
                        )
                    }
                }
            }
            if (newStepIndex != null && oldStepIndex != newStepIndex) {
                add(
                    ExecutionEvent.StepStarted(
                        sessionId = new.session.id,
                        stepIndex = newStepIndex
                    )
                )
            }
            if (new.state == FinishedState && old.state != FinishedState) {
                add(
                    ExecutionEvent.WorkoutFinished(
                        new.session.id,
                        now
                    )
                )
            }
        }
    }
}