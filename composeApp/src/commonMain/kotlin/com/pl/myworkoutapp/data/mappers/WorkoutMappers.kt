package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.ExerciseEntity
import com.pl.myworkoutapp.domain.model.exercise.CustomExercise
import com.pl.myworkoutapp.domain.model.exercise.Exercise

fun ExerciseEntity.toDomain(): CustomExercise {
    TODO()
//    return BuiltInExercise(
//        builtInId = TODO(),
//        muscle = TODO(),
//        exerciseType = TODO(),
//        equipment = TODO(),
//        met = TODO(),
//        quantityType = TODO()
//    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = TODO(),
        name = TODO(),
        met = TODO()
    )
}