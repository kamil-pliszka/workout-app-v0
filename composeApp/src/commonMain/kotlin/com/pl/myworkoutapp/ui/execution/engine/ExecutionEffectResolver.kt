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
            addAll(resolveHalfTime(old, action, new))
            // inne efekty...
        }
    }

    private fun resolveStart(
        old: WorkoutExecutionRuntime,
        new: WorkoutExecutionRuntime,
    ): List<ExecutionEffect> {
        if (
            old.phase != ExecutionPhase.Exercise &&
            new.phase == ExecutionPhase.Exercise
        ) {
            return listOf(
                ExecutionEffect.Vibrate(),
                ExecutionEffect.PlaySound(SoundType.START)
            )
        }
        return emptyList()
    }


    private fun resolveHalfTime(
        old: WorkoutExecutionRuntime,
        action: ExecutionAction,
        new: WorkoutExecutionRuntime,
    ): List<ExecutionEffect> {
        if (action != ExecutionAction.Tick) {
            return emptyList()
        }

        if (new.phase != ExecutionPhase.Exercise) {
            return emptyList()
        }

        val step = new.currentStep as? ExecutionStep.ExerciseStep
            ?: return emptyList()

        if (step.quantity.type != QuantityType.DURATION) {
            return emptyList()
        }

        val total = step.quantity.value

        if (total < 20) {
            return emptyList()
        }

        val oldRemaining = old.remainingSeconds ?: return emptyList()
        val newRemaining = new.remainingSeconds ?: return emptyList()

        val half = total / 2

        @Suppress("ConvertTwoComparisonsToRangeCheck")//dla mnie bardziej czytelna wersja
        return if (oldRemaining > half && newRemaining <= half) {
            listOf(
                ExecutionEffect.Speak(SpeechText.HalfTime)
            )
        } else {
            emptyList()
        }
    }
}