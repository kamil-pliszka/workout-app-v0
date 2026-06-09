package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession

class WorkoutExecutionRuntimeFactory(
    private val planBuilder: ExecutionPlanBuilder
) {
    fun create(
        workout: Workout,
        exercises: Set<Exercise>, //resolved exercises
        session: WorkoutSession,
        weightKg: Double
    ): WorkoutExecutionRuntime {
        val executionPlan = planBuilder.build(
            workout,
            exercises
        )
        return WorkoutExecutionRuntime(
            workout = workout,
            session = session,
            plan = executionPlan,
            state = when {
                else -> EntryState(
                    remainingSeconds = if (session.currentStepIndex != null) 5 else 10, //TODO - ustawienia usera/konfiguracja
                    startStepIndex = session.currentStepIndex ?: 0
                )
            },
            weightKg = weightKg
        )
    }
}