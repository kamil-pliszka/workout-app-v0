package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.ExerciseEntity
import com.pl.myworkoutapp.domain.model.Exercise

fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        id = TODO(),
        name = TODO(),
        category = TODO(),
        difficulty = TODO(),
        durationSeconds = TODO(),
        met = TODO()
    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = TODO(),
        name = TODO(),
        category = TODO(),
        met = TODO()
    )
}