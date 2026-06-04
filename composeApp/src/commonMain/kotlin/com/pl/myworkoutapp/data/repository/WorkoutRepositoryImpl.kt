package com.pl.myworkoutapp.data.repository

import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.data.database.ExerciseDao
import com.pl.myworkoutapp.data.database.WorkoutDao
import com.pl.myworkoutapp.data.mappers.*
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlansRegistry
import com.pl.myworkoutapp.domain.model.plan.PlanId
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.domain.model.plan.asString
import com.pl.myworkoutapp.domain.model.workout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map
import kotlin.time.Instant

//TODO - rozdzielić na 2 repo
class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val flatteningMapper: WorkoutFlatteningMapper,
    private val workoutTreeBuilder: WorkoutEntityTreeBuilder,
) : WorkoutRepository {
    override fun observeExercises(): Flow<List<Exercise>> {
//        return workoutDao.observeExercises().map {
//            list -> list.map { it.toDomain() }
//        }
        TODO()
    }

    override suspend fun getCustomExercises(): List<CustomExercise> = withContext(Dispatchers.IO) {
        exerciseDao.getAllExercises().map {
            it.toDomain()
        }
    }

    override suspend fun getBuiltinExercises(): List<BuiltInExercise> =
        withContext(Dispatchers.IO) {
            BuiltInExerciseRegistry.getAll().toList()
        }

    override suspend fun getAllExercises(): List<Exercise> =
        getCustomExercises() + getBuiltinExercises()

    override suspend fun getPlans(): List<TrainingPlan> {
        return BuiltInTrainingPlansRegistry.getAll()
    }

    override suspend fun savePlan(plan: TrainingPlan) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSession(session: WorkoutSession): WorkoutSession {
        val generatedId = workoutDao.insertSession(
            session.toEntity().copy(updatedAt = currentTimeMilliseconds())
        )
        println("saveSession sourceId: ${session.id}, generatedId: $generatedId")
        return session.copy(
            id = generatedId.takeIf { it > 0 } ?: session.id
        )
    }

    override suspend fun updateSession(session: WorkoutSession) {
        val updated = workoutDao.updateSession(
            session.toEntity().copy(updatedAt = currentTimeMilliseconds())
        )
        require(updated > 0) {
            "WorkoutSession not found: ${session.id}"
        }
    }

    override suspend fun getHistory(): List<WorkoutSession> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkouts(): List<Workout> {
        //TODO - uwzględnić pozostałe typy workout
        return BuiltInWorkoutRegistry.getAll()
    }

    override suspend fun getWorkout(workoutId: WorkoutId): Workout {
        return when (workoutId) {
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
        return when (exerciseId) {
            is ExerciseId.BuiltIn -> BuiltInExerciseRegistry.get(exerciseId.id)
            is ExerciseId.Custom -> {
                exerciseDao.getExerciseById(exerciseId.toLong())?.toDomain()
                    ?: error("Exercise not found: $exerciseId")
            }
        }
    }

    override suspend fun saveCustomWorkout(customWorkout: CustomWorkout): WorkoutId.Custom {
        //println("saveCustomWorkout, eq check: ${WorkoutId.Custom.NEW == 0L.asWorkoutId()}")
        //val flatItems = mutableListOf<FlatWorkoutItem>()
        //flatten(customWorkout.items, flatItems, null)
        val flatItems = flatteningMapper.flatten(customWorkout.items)
        println("FLATTEN:")
        flatItems.forEachIndexed { index, item ->
            println("idx = $index, pos = ${item.position}, parent = ${item.parentIndex} : ${item.itemEntity.type}, ${item.itemEntity.builtInExerciseId}:${item.itemEntity.customExerciseId}")
        }
        if (customWorkout.id.isNew()) { //insert as Custom
            return workoutDao.insertWorkout(customWorkout.toEntity(), flatItems)
        } else { //update Custom
            return workoutDao.updateWorkout(customWorkout.toEntity(), flatItems)
        }
    }

    override suspend fun saveCustomExercise(customExercise: CustomExercise): ExerciseId.Custom {
        if (customExercise.id == ExerciseId.Custom.NEW) { //insert as Custom
            return exerciseDao.insertExercise(customExercise.toEntity())
        } else { //update Custom
            return exerciseDao.updateExercise(customExercise.toEntity())
        }
    }

    override fun observeCustomExercises(): Flow<List<CustomExercise>> =
        exerciseDao.observeExercises().map { list ->
            list.map { it.toDomain() }
        }

    override fun observeLatestBasedOn(builtinIds: Set<WorkoutId.BuiltIn>): Flow<List<CustomWorkout>> {
        return workoutDao.observeLatestBasedOn(
            builtinIds.map { it.asRawString() }.toSet()
        ).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLatestBasedOnIds(builtinIds: Set<WorkoutId.BuiltIn>): Flow<List<WorkoutId.Custom>> {
        return workoutDao.observeLatestBasedOnIds(
            builtinIds.map { it.asRawString() }.toSet()
        ).map { listOfIds ->
            listOfIds.map { it.asWorkoutId() }
        }
    }

    override fun observeMainCustomWorkouts(): Flow<List<CustomWorkout>> {
        return workoutDao.observeMainCustomWorkouts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeMainCustomWorkoutsIds(): Flow<List<WorkoutId.Custom>> {
        return workoutDao.observeMainCustomWorkoutsIds().map { listOfIds ->
            listOfIds.map { it.asWorkoutId() }
        }
    }

    override suspend fun findLatestBasedOn(builtInId: WorkoutId.BuiltIn): CustomWorkout? {
        return workoutDao.findLatestBasedOn(builtInId.asRawString())?.toDomain()
    }

    override suspend fun deleteWorkout(id: WorkoutId.Custom) {
        workoutDao.deleteById(id.toLong())
    }

    override suspend fun findLatestWorkoutSession(
        planId: PlanId?,
        workoutId: WorkoutId
    ): WorkoutSession? {
        val session = if (planId == null) {
            workoutDao.findLatestWorkoutSession(workoutId.asString())
        } else {
            workoutDao.findLatestWorkoutSession(workoutId.asString(), planId.asString())
        }
        return session?.toDomain()
    }

    override suspend fun updateSessionCurrentStep(
        sessionId: Long,
        currentStepIndex: Int
    ) {
        workoutDao.updateSessionCurrentStep(sessionId, currentStepIndex)
    }

    override suspend fun finishWorkoutSession(
        sessionId: Long,
        endTime: Instant
    ) {
        workoutDao.finishWorkoutSession(sessionId, endTime)
    }

    override suspend fun insertPerformedExercise(performedExercise: PerformedExercise) {
        workoutDao.insertPerformedExercise(performedExercise.toEntity())
    }

    override suspend fun getPerformedExercises(sessionId: Long) : List<PerformedExercise> {
        return workoutDao.getPerformedExercises(sessionId).map {
            it.toDomain()
        }
    }

    override suspend fun getWorkoutSession(sessionId: Long): WorkoutSession {
        val sessionEntity = workoutDao.getWorkoutSessionById(sessionId)
        require(sessionEntity != null) {
            "WorkoutSession not found: $sessionId"
        }
        return sessionEntity.toDomain()
    }

    override suspend fun completeExercise(
        performedExercise: PerformedExercise,
        sessionUpdate: WorkoutSession
    ) {
        workoutDao.completeExercise(performedExercise, sessionUpdate)
    }
}

