package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*


val ARM_EXERCISES = listOf(
    BuiltInExercise(
        id =  BuiltInExerciseId.DUMBELL_BICEPS_CURLS.asExerciseId(),
        muscle = MuscleGroup.ARMS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.DUMBBELLS,
        met = 3.5,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 10,
    ),

    BuiltInExercise(
        id =  BuiltInExerciseId.TRICEPS_DIPS_ON_CHAIR.asExerciseId(),
        muscle = MuscleGroup.ARMS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 6.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 10,
    ),
)