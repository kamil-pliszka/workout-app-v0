package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.CustomExerciseEntity
import com.pl.myworkoutapp.domain.model.exercise.*

fun String.toBuiltInExerciseId(): ExerciseId.BuiltIn =
    BuiltInExerciseId.valueOf(this).asExerciseId()

fun ExerciseId.BuiltIn.asRawString() = this.id.name
fun CustomExercise.toEntity() = CustomExerciseEntity(
    id = id.toLong(),
    name = name,
    description = description,
    imageUri = imageUri,
    basedOn = basedOn?.asRawString(),
    muscle = muscle.name,
    exerciseType = exerciseType.name,
    equipment = equipment.name,
    quantityType = quantityType.name,
    met = met,
    defaultQuantityVal = defaultQuantityValue,
    secondsPerRep = secondsPerRep,
    metersPerSecond = metersPerSecond,
)


fun CustomExerciseEntity.toDomain() = CustomExercise(
    id = id.asExerciseId(),
    name = name,
    description = description,
    imageUri = imageUri,
    basedOn = basedOn?.toBuiltInExerciseId(),
    muscle = MuscleGroup.valueOf(muscle),
    exerciseType = ExerciseType.valueOf(exerciseType),
    equipment = Equipment.valueOf(equipment),
    met = met,
    quantityType = QuantityType.valueOf(quantityType),
    defaultQuantityValue = defaultQuantityVal,
    secondsPerRep = secondsPerRep,
    metersPerSecond = metersPerSecond,
)
