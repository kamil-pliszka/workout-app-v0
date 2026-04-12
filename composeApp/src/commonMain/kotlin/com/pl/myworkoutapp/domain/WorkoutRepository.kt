package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.CustomExercise
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession
import kotlinx.coroutines.flow.Flow


interface WorkoutRepository {

    fun observeExercises(): Flow<List<Exercise>>
    suspend fun getCustomExercises(): List<CustomExercise>
    suspend fun getBuiltinExercises(): List<BuiltInExercise>
    suspend fun getAllExercises(): List<Exercise>

    suspend fun getPlans(): List<TrainingPlan>

    suspend fun savePlan(plan: TrainingPlan)

    suspend fun saveSession(session: WorkoutSession)

    suspend fun getHistory(): List<WorkoutSession>
    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkout(workoutId : WorkoutId): Workout

    suspend fun getExercise(exerciseId: ExerciseId): Exercise
    suspend fun saveCustomWorkout(customWorkout: CustomWorkout): WorkoutId.Custom
}
