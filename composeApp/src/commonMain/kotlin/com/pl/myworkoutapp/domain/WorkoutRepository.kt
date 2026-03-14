package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.Exercise
import com.pl.myworkoutapp.domain.model.WorkoutPlan
import com.pl.myworkoutapp.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow


interface WorkoutRepository {

    fun observeExercises(): Flow<List<Exercise>>
    suspend fun getAllExercises(): List<Exercise>

    suspend fun getPlans(): List<WorkoutPlan>

    suspend fun savePlan(plan: WorkoutPlan)

    suspend fun saveSession(session: WorkoutSession)

    suspend fun getHistory(): List<WorkoutSession>
}
