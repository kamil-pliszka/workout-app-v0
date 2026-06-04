package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.PerformedExerciseEntity
import com.pl.myworkoutapp.data.database.WorkoutSessionEntity
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Quantity
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.domain.model.plan.asString
import com.pl.myworkoutapp.domain.model.plan.toPlanIdOrNull
import com.pl.myworkoutapp.domain.model.workout.PerformedExercise
import com.pl.myworkoutapp.domain.model.workout.WorkoutSession
import com.pl.myworkoutapp.domain.model.workout.asString
import com.pl.myworkoutapp.domain.model.workout.toWorkoutIdOrNull

fun WorkoutSessionEntity.toDomain() = WorkoutSession(
    id = id,
    planId = planId?.let {
        it.toPlanIdOrNull() ?: error("Incorrect planId: $it")
    },
    workoutId = workoutId.toWorkoutIdOrNull() ?: error("Incorrect wokroutId: $workoutId"),
    startTime = startTime,
    endTime = endTime,
    progress = progress,
    completed = completed,
    estimatedDuration = estimatedDuration,
    estimatedCalories = estimatedCalories,
    executionTime = executionTime,
    calories = calories,
    currentStepIndex = currentStepIndex,
    performedExercises = emptyList(),
)


fun WorkoutSession.toEntity() = WorkoutSessionEntity(
    id = id,
    planId = planId?.asString(),
    workoutId = workoutId.asString(),
    startTime = startTime,
    endTime = endTime,
    progress = progress,
    completed = completed,
    estimatedDuration = estimatedDuration,
    estimatedCalories = estimatedCalories,
    executionTime = executionTime,
    currentStepIndex = currentStepIndex,
    calories = calories,
)

fun PerformedExerciseEntity.toDomain() = PerformedExercise(
    id = id,
    sessionId = sessionId,
    exerciseId = when {
        builtInExerciseId != null -> BuiltInExerciseId.valueOf(builtInExerciseId).asExerciseId()
        customExerciseId != null -> customExerciseId.asExerciseId()
        else -> error("empty exerciseId")
    },
    plannedQuantity = Quantity(
        type = QuantityType.valueOf(quantityType),
        value = plannedQuantityValue
    ),
    actualQuantityValue = actualQuantityValue,
    startTime = startTime,
    endTime = endTime,
    calories = calories
)

fun PerformedExercise.toEntity() = PerformedExerciseEntity(
    id = id,
    sessionId = sessionId,
    //exerciseId = exerciseId.asString(),
    builtInExerciseId = when (exerciseId) {
        is ExerciseId.BuiltIn -> exerciseId.id.name
        else -> null
    },
    customExerciseId = when (exerciseId) {
        is ExerciseId.Custom -> exerciseId.id
        else -> null
    },
    quantityType = plannedQuantity.type.name,
    plannedQuantityValue = plannedQuantity.value,
    actualQuantityValue = actualQuantityValue,
    startTime = startTime,
    endTime = endTime,
    calories = calories
)