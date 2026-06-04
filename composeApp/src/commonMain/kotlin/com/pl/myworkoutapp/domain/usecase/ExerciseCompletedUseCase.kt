package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import com.pl.myworkoutapp.domain.model.workout.PerformedExercise
import kotlin.time.Instant

class ExerciseCompletedUseCase(
    private val workoutRepository: WorkoutRepository,
) {
    suspend fun execute(
        weightKg: Double,
        sessionId: Long,
        exercise: Exercise,
        quantity: Quantity,
        startTime: Instant,
        endTime: Instant,
        progress: Float,
    ) {
        //czas w sekundach
        val duration = (endTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()) / 1000.0
        val calories = calculateKcal(
            durationSeconds = duration,
            met = exercise.met,
            weightKg = weightKg
        )

        val session = workoutRepository.getWorkoutSession(sessionId)

        val newSession = session.copy(
            executionTime = session.executionTime + duration,
            calories = session.calories + calories,
            progress = progress,
        )

        val performedExercise = PerformedExercise(
            id = 0,
            sessionId = sessionId,
            exerciseId = exercise.id,
            plannedQuantity = quantity,
            actualQuantityValue = quantity.value,
            startTime = startTime,
            endTime = endTime,
            calories = calories,
        )


        workoutRepository.completeExercise(performedExercise, newSession)
    }

    //kcal = MET × masa_kg × czas_h
    private fun calculateKcal(durationSeconds: Double, met: Double, weightKg: Double): Double {
        val hours = durationSeconds / 3600.0
        return met * weightKg * hours
    }

}