package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*

val LEGS_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.JUMPING_JACKS.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 8.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SQUATS.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 5.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 15,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.RUNNING.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.MOBILITY,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DISTANCE,//albo DURATION
        defaultQuantityValue = 1000,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.RUNNING_ON_TIME.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.MOBILITY,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 10 * 60,//10 min
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.WALKING.asExerciseId(), //"Chód/Spacer",
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 0.5,
        quantityType = QuantityType.DISTANCE,//albo DURATION
        defaultQuantityValue = 30 * 60,//30 min
    ),
)