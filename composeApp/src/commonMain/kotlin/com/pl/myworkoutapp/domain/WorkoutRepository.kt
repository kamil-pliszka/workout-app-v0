package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession
import kotlinx.coroutines.flow.Flow


interface WorkoutRepository {

    fun observeExercises(): Flow<List<Exercise>>
    suspend fun getAllExercises(): List<Exercise>

    suspend fun getPlans(): List<TrainingPlan>

    suspend fun savePlan(plan: TrainingPlan)

    suspend fun saveSession(session: WorkoutSession)

    suspend fun getHistory(): List<WorkoutSession>
}
