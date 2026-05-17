package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.WorkoutSessionEntity
import com.pl.myworkoutapp.domain.model.plan.asString
import com.pl.myworkoutapp.domain.model.plan.toPlanIdOrNull
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
    calories = calories,
)