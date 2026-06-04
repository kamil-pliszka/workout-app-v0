package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import kotlin.time.Instant

sealed interface ExecutionEvent {
    data class ExerciseCompleted(
        val sessionId: Long,
        val exercise: Exercise,
        val quantity: Quantity,
        val startTime: Instant,
        val endTime: Instant,
        val progress: Float,
        val weightKg: Double,
    ) : ExecutionEvent

    data class WorkoutFinished(
        val sessionId: Long,
        val endTime: Instant,
    ) : ExecutionEvent

    data class StepStarted(
        val sessionId: Long,
        val stepIndex: Int
    ) : ExecutionEvent
}