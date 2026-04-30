package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*


val ABS_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.RUSSIAN_TWIST.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 4.0,
        quantityType = QuantityType.REPS_PER_SIDE,
        defaultQuantityValue = 15,
        secondsPerRep = 1.4,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.FLUTTER_KICKS.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 4.5,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.REVERSE_CRUNCHES.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 4.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 20,
        secondsPerRep = 2.0,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.HEEL_TOUCH.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.8,
        quantityType = QuantityType.REPS_PER_SIDE,
        defaultQuantityValue = 15,
        secondsPerRep = 1.2,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.DEAD_BUG.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.5,
        quantityType = QuantityType.REPS_PER_SIDE,
        defaultQuantityValue = 10,
        secondsPerRep = 2.2,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.BUTT_BRIDGE.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.8,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 15,
        secondsPerRep = 2.5,
        metersPerSecond = null,
    ),


    )