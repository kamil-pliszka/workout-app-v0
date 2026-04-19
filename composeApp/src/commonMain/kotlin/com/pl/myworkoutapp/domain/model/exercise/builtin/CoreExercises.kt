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
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_PLANK_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 40,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_PLANK_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 40,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.BENT_LEG_TWIST.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS_PER_SIDE,
        defaultQuantityValue = 10,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_CRUNCHES_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 20,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 20,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.HOLLOW_BODY.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.PUSH_UP_HOLD_DOWN.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
        defaultQuantityValue = 30,
    ),


)