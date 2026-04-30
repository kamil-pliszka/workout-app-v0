package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.*

val CHEST_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.PUSH_UP.asExerciseId(),
        muscle = MuscleGroup.CHEST,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 7.0,
        quantityType = QuantityType.REPS,
        defaultQuantityValue = 10,
        // ~2s w dół + ~1s w górę + krótka stabilizacja
        secondsPerRep = 3.0,
        metersPerSecond = null,
    ),
)