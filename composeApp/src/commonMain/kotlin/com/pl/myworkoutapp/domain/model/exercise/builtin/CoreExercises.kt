package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*

val CORE_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.PLANK.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.5,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 45,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SIDE_PLANK_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 40,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SIDE_PLANK_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 40,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.BENT_LEG_TWIST.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 4.0,
        quantityType = QuantityType.REPS_PER_SIDE,
        defaultQuantityValue = 10,
        // rotacja L+P z kontrolą
        secondsPerRep = 2.0,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SIDE_CRUNCHES_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.8,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 20,
        secondsPerRep = 2.5,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.8,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 20,
        secondsPerRep = 2.5,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 5.5,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.HOLLOW_BODY.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 4.5,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.PUSH_UP_HOLD_DOWN.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 6.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    )