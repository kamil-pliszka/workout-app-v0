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

    val plan: ExecutionPlan,
    val state: RuntimeState,
    val weightKg: Double,
) {
    fun currentExecutionStepOrNull(): ExecutionStep? {
        val index = state.stepIndexOrNull() ?: return null
        return plan.steps.getOrNull(index)
    }

    //TODO - progres do przebudowy po uwzględnieniu Circuits
    fun calculateProgress(): Float {
        if (state is FinishedState) {
            return 1f
        }
        val exerciseSteps = plan.steps.filterIsInstance<ExecutionStep.ExerciseStep>()
        if (exerciseSteps.isEmpty()) {
            return 0f
        }
        val currentIndex = state.stepIndexOrNull() ?: return 0f
        val completedExercises =
            plan.steps.countIndexedBefore(currentIndex) {
                it is ExecutionStep.ExerciseStep
            }

        return completedExercises.toFloat() / exerciseSteps.size
    }

    fun nextExerciseOrNull(fromStepIndex: Int) : ExecutionStep.ExerciseStep? =  plan.steps
        .drop(fromStepIndex)
        .filterIsInstance<ExecutionStep.ExerciseStep>()
        .firstOrNull()
}

inline fun <T> List<T>.countIndexedBefore(
    index: Int,
    predicate: (T) -> Boolean
): Int {
    if (index <= 0) {
        return 0
    }
    return take(index).count(predicate)
}