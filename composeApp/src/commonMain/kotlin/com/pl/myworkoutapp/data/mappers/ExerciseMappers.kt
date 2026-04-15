package com.pl.myworkoutapp.data.mappers

import com.pl.myworkoutapp.data.database.ExerciseEntity
import com.pl.myworkoutapp.domain.model.exercise.*

fun String.toBuiltInExerciseId(): ExerciseId.BuiltIn = BuiltInExerciseId.valueOf(this).asExerciseId()
fun ExerciseId.BuiltIn.asRawString() = this.id.name
fun CustomExercise.toEntity() = ExerciseEntity(
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
)


fun ExerciseEntity.toDomain() = CustomExercise(
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
)
