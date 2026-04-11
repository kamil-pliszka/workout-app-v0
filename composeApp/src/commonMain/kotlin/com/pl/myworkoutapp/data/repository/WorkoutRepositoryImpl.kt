package com.pl.myworkoutapp.data.repository

import com.pl.myworkoutapp.data.database.WorkoutDao
import com.pl.myworkoutapp.data.mappers.toDomain
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.domain.model.exercise.CustomExercise
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlansRegistry
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
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
    override suspend fun getCustomExercises(): List<CustomExercise> = withContext(Dispatchers.IO) {
        workoutDao.getAllExercises().map {
            it.toDomain()
        }
    }

    override suspend fun getBuiltinExercises(): List<BuiltInExercise> = withContext(Dispatchers.IO) {
        BuiltInExerciseRegistry.getAll().toList()
    }

    override suspend fun getAllExercises(): List<Exercise> = getCustomExercises() + getBuiltinExercises()

    override suspend fun getPlans(): List<TrainingPlan> {
        return BuiltInTrainingPlansRegistry.getAll()
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

    override suspend fun getWorkouts(): List<Workout> {
        //TODO - uwzględnić pozostałe typy workout
        return BuiltInWorkoutRegistry.getAll()
    }

    override suspend fun getWorkout(workoutId: WorkoutId): Workout {
        return when(workoutId) {
            is WorkoutId.BuiltIn -> BuiltInWorkoutRegistry.get(workoutId.id)
            is WorkoutId.Custom -> TODO()
        }
    }

    override suspend fun getExercise(exerciseId: ExerciseId): Exercise {
        return when(exerciseId) {
            is ExerciseId.BuiltIn -> BuiltInExerciseRegistry.get(exerciseId.id)
            is ExerciseId.Custom -> TODO()
        }
    }
}