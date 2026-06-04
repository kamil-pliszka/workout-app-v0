package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.ui.effects.SoundType

class ExecutionEffectResolver {

    fun resolve(
        old: WorkoutExecutionRuntime,
        action: ExecutionAction,
        new: WorkoutExecutionRuntime
    ): List<ExecutionEffect> {
        return buildList {
            addAll(resolveStart(old, new))
            if (isHalfTime(old, action, new)) {
                add(
                    ExecutionEffect.Speak(SpeechText.HalfTime)
                )
            }
            // inne efekty...
        }
    }

    private fun resolveStart(
        old: WorkoutExecutionRuntime,
        new: WorkoutExecutionRuntime,
    ): List<ExecutionEffect> {
        if (
            old.state !is ExerciseState &&
            new.state is ExerciseState
        ) {
            return listOf(
                ExecutionEffect.Vibrate(),
                ExecutionEffect.PlaySound(SoundType.START)
            )
        }
        return emptyList()
    }


    private fun isHalfTime(
        old: WorkoutExecutionRuntime,
        action: ExecutionAction,
        new: WorkoutExecutionRuntime,
    ): Boolean {
        if (action != ExecutionAction.Tick) {
            return false
        }

        if (new.state !is ExerciseState) {
            return false
        }
        if (old.state !is ExerciseState) {
            return false
        }

        val step = new.currentExecutionStepOrNull() as? ExecutionStep.ExerciseStep ?: return false

        if (step.quantity.type != QuantityType.DURATION) {
            return false
        }

        val total = step.quantity.value

        if (total < 20) {
            return false
        }

        val oldRemaining = (old.state.targetState as? ExerciseTargetState.Countdown)?.remainingSeconds
            ?: return false
        val newRemaining = (new.state.targetState as? ExerciseTargetState.Countdown)?.remainingSeconds
            ?: return false

        val half = total / 2

        @Suppress("ConvertTwoComparisonsToRangeCheck")//dla mnie bardziej czytelna wersja
        return (oldRemaining > half && newRemaining <= half)
    }
}