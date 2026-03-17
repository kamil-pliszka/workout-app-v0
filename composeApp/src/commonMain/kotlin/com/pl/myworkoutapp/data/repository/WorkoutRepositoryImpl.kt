package com.pl.myworkoutapp.data.repository

import com.pl.myworkoutapp.data.database.WorkoutDao
import com.pl.myworkoutapp.data.mappers.toDomain
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override fun observeExercises(): Flow<List<Exercise>> {
        return workoutDao.observeExercises().map {
            list -> list.map { it.toDomain() }
        }
    }
    override suspend fun getAllExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        workoutDao.getAllExercises().map {
            it.toDomain()
        }
    }

    override suspend fun getPlans(): List<TrainingPlan> {
        TODO("Not yet implemented")
    }

    override suspend fun savePlan(plan: TrainingPlan) {
        TODO("Not yet implemented")
    }

    override suspend fun saveSession(session: WorkoutSession) {
        TODO("Not yet implemented")
    }

    override suspend fun getHistory(): List<WorkoutSession> {
        TODO("Not yet implemented")
    }
}