package com.pl.myworkoutapp.domain.usecase

import com.pl.myworkoutapp.domain.WorkoutHydrator
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.plan.PlanId
import com.pl.myworkoutapp.domain.model.workout.*
import kotlin.math.roundToInt
import kotlin.time.Clock

data class PrepareWorkoutExecutionResult(
    val workout: Workout,
    val exercises: Set<Exercise>, //resolved exercises
    val session: WorkoutSession
)

class PrepareWorkoutExecutionUseCase(
    private val hydrator: WorkoutHydrator,
    private val resolveWorkoutExerciseUC: ResolveWorkoutExercisesUseCase,
    private val repository: WorkoutRepository,
) {
    suspend fun execute(
        planId: PlanId?,
        workoutId: WorkoutId,
        weightKg: Double
    ): PrepareWorkoutExecutionResult {
        val hydrated = hydrator.hydrate(workoutId)
        val exercises = resolveWorkoutExerciseUC.execute(hydrated.items)
        val estimatedKcal = (hydrated.baseKcalPerKg * weightKg).roundToInt()

        val latestSession = repository.findLatestWorkoutSession(planId, workoutId)
        val session = if (latestSession == null || latestSession.completed) {
            repository.insertSession(
                prepareInitialSession(
                    planId, hydrated, estimatedKcal
                )
            )
        } else {
            latestSession
        }

        return PrepareWorkoutExecutionResult(
            workout = hydrated,
            exercises = exercises,
            session = session
        )
    }

    private fun prepareInitialSession(planId: PlanId?, workout: Workout, estimatedKcal: Int) =
        WorkoutSession(
            id = 0,
            planId = planId,
            workoutId = workout.id,
            startTime = Clock.System.now(),
            endTime = null,
            progress = 0f,
            completed = false,
            estimatedDuration = workout.estimatedDuration,
            estimatedCalories = estimatedKcal,
            executionTime = 0.0,
            calories = 0.0,
            currentStepIndex = null,
            performedExercises = emptyList(),
        )

}