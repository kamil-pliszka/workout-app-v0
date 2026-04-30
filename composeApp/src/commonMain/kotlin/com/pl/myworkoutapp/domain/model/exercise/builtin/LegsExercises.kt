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

        // szybkie tempo całego ciała
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SQUATS.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 5.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 15,

        // ~3s / squat (zejście + wejście + stabilizacja)
        secondsPerRep = 3.0,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.RUNNING.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 9.8,
        quantityType = QuantityType.DISTANCE,

        defaultQuantityValue = 1000,

        // średni bieg ~ 2.8 m/s (ok. 10 km/h)
        secondsPerRep = null,
        metersPerSecond = 2.8,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.RUNNING_ON_TIME.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 9.0,
        quantityType = QuantityType.DURATION,

        defaultQuantityValue = 10 * 60,

        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.WALKING.asExerciseId(),
        muscle = MuscleGroup.LEGS,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 3.3,
        quantityType = QuantityType.DISTANCE,

        defaultQuantityValue = 30 * 60,

        // szybki spacer ~ 1.4 m/s (ok. 5 km/h)
        secondsPerRep = null,
        metersPerSecond = 1.4,
    ),
)