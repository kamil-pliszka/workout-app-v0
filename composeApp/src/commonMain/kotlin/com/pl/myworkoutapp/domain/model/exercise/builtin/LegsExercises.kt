package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId

val LEGS_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.JUMPING_JACKS.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 8.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SQUATS.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 5.0,
        quantityType = QuantityType.REPS,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.RUNNING.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.MOBILITY,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DISTANCE,//albo DURATION
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.RUNNING_ON_TIME.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.MOBILITY,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.WALKING.asExerciseId(), //"Chód/Spacer",
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 0.5,
        quantityType = QuantityType.DISTANCE,//albo DURATION
    ),
)