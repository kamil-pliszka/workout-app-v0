package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.usecase.ExerciseCompletedUseCase

class ExecutionEventHandler(
    private val workoutRepository: WorkoutRepository,
    private val exerciseCompletedUC: ExerciseCompletedUseCase,
) {

    suspend fun handle(event: ExecutionEvent) {
        when (event) {
            is ExecutionEvent.StepStarted ->
                handleStepStarted(event)

            is ExecutionEvent.ExerciseCompleted ->
                handleExerciseCompleted(event)

            is ExecutionEvent.WorkoutFinished ->
                handleWorkoutFinished(event)
        }
    }

    private suspend fun handleStepStarted(
        event: ExecutionEvent.StepStarted
    ) {
        workoutRepository.updateSessionCurrentStep(
            sessionId = event.sessionId,
            currentStepIndex = event.stepIndex
        )
    }

    private suspend fun handleExerciseCompleted(
        event: ExecutionEvent.ExerciseCompleted
    ) {
        exerciseCompletedUC.execute(
            weightKg = event.weightKg,
            sessionId = event.sessionId,
            exercise = event.exercise,
            quantity = event.quantity,
            startTime = event.startTime,
            endTime = event.endTime,
            progress = event.progress
        )
    }

    private suspend fun handleWorkoutFinished(
        event: ExecutionEvent.WorkoutFinished
    ) {
        workoutRepository.finishWorkoutSession(
            sessionId = event.sessionId,
            endTime = event.endTime
        )
    }
}