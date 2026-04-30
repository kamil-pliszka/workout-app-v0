package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*


val BACK_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.COBRA_STRETCH.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRETCH,
        equipment = Equipment.BODYWEIGHT,
        met = 2.3,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.SUPERMAN.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.8,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 15,
        secondsPerRep = 2.5,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.V_HOLD.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 4.2,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

    BuiltInExercise(
        id = BuiltInExerciseId.CHILD_POSE.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRETCH,
        equipment = Equipment.BODYWEIGHT,
        met = 2.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
        secondsPerRep = null,
        metersPerSecond = null,
    ),

)