package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.plan.PlanId
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant


interface WorkoutRepository {

    fun observeExercises(): Flow<List<Exercise>>
    suspend fun getCustomExercises(): List<CustomExercise>
    suspend fun getBuiltinExercises(): List<BuiltInExercise>
    suspend fun getAllExercises(): List<Exercise>

    suspend fun getPlans(): List<TrainingPlan>

    suspend fun savePlan(plan: TrainingPlan)

    suspend fun insertSession(session: WorkoutSession) : WorkoutSession
    suspend fun updateSession(session: WorkoutSession)

    suspend fun getHistory(): List<WorkoutSession>
    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(workoutId: WorkoutId): Workout

    suspend fun getExercise(exerciseId: ExerciseId): Exercise
    suspend fun saveCustomWorkout(customWorkout: CustomWorkout): WorkoutId.Custom

    suspend fun saveCustomExercise(customExercise: CustomExercise): ExerciseId.Custom
    fun observeCustomExercises(): Flow<List<CustomExercise>>
    fun observeLatestBasedOn(builtinIds: Set<WorkoutId.BuiltIn>): Flow<List<CustomWorkout>>
    fun observeLatestBasedOnIds(builtinIds: Set<WorkoutId.BuiltIn>): Flow<List<WorkoutId.Custom>>
    fun observeMainCustomWorkouts(): Flow<List<CustomWorkout>>
    fun observeMainCustomWorkoutsIds(): Flow<List<WorkoutId.Custom>>
    suspend fun findLatestBasedOn(builtInId: WorkoutId.BuiltIn): CustomWorkout?
    suspend fun deleteWorkout(id: WorkoutId.Custom)
    suspend fun findLatestWorkoutSession(planId: PlanId?, workoutId: WorkoutId): WorkoutSession?
    suspend fun updateSessionCurrentStep(sessionId: Long, currentStepIndex: Int)
    suspend fun finishWorkoutSession(sessionId: Long, endTime: Instant)
    suspend fun insertPerformedExercise(performedExercise: PerformedExercise)
    suspend fun getPerformedExercises(sessionId: Long): List<PerformedExercise>
    suspend fun getWorkoutSession(sessionId: Long): WorkoutSession

    suspend fun completeExercise(
        performedExercise: PerformedExercise,
        sessionUpdate: WorkoutSession
    )
}
