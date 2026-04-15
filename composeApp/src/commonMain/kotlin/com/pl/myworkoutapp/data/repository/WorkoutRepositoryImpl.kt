package com.pl.myworkoutapp.data.repository

import com.pl.myworkoutapp.data.database.WorkoutDao
import com.pl.myworkoutapp.data.mappers.*
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlansRegistry
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao,
    private val flatteningMapper: WorkoutFlatteningMapper,
    private val workoutTreeBuilder: WorkoutTreeBuilder,
) : WorkoutRepository {
    override fun observeExercises(): Flow<List<Exercise>> {
//        return workoutDao.observeExercises().map {
//            list -> list.map { it.toDomain() }
//        }
        TODO()
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
            is WorkoutId.Custom -> getCustomWorkout(workoutId.id)
        }
    }

    private suspend fun getCustomWorkout(workoutId: Long): CustomWorkout {
        val workoutEntity = workoutDao.getWorkoutById(workoutId)
        require(workoutEntity != null) {
            "Workout not found: $workoutId"
        }

        val flatSortedItems = workoutDao.getSortedItemsByWorkoutId(workoutEntity.id)

        val domainWorkout = workoutEntity.toDomain()
        val roots = workoutTreeBuilder.build(flatSortedItems)
        return domainWorkout.copy(items = roots)
    }

    override suspend fun getExercise(exerciseId: ExerciseId): Exercise {
        return when(exerciseId) {
            is ExerciseId.BuiltIn -> BuiltInExerciseRegistry.get(exerciseId.id)
            is ExerciseId.Custom -> {
                workoutDao.getExerciseById(exerciseId.toLong())?.toDomain()
                    ?: error("Exercise not found: $exerciseId")
            }
        }
    }

    override suspend fun saveCustomWorkout(customWorkout: CustomWorkout) : WorkoutId.Custom {
        //println("saveCustomWorkout, eq check: ${WorkoutId.Custom.NEW == 0L.asWorkoutId()}")
        //val flatItems = mutableListOf<FlatWorkoutItem>()
        //flatten(customWorkout.items, flatItems, null)
        val flatItems = flatteningMapper.flatten(customWorkout.items)
        println("FLATTEN:")
        flatItems.forEachIndexed { index, item ->
            println("idx = $index, pos = ${item.position}, parent = ${item.parentIndex} : ${item.itemEntity.type}, ${item.itemEntity.exerciseId}")
        }
        if (customWorkout.id == WorkoutId.Custom.NEW) { //insert as Custom
            return workoutDao.insertWorkout(customWorkout.toEntity(), flatItems)
        } else { //update Custom
            return workoutDao.updateWorkout(customWorkout.toEntity(), flatItems)
        }
    }

    override suspend fun saveCustomExercise(customExercise: CustomExercise): ExerciseId.Custom {
        if (customExercise.id == ExerciseId.Custom.NEW) { //insert as Custom
            return workoutDao.insertExercise(customExercise.toEntity())
        } else { //update Custom
            return workoutDao.updateExercise(customExercise.toEntity())
        }
    }
}

