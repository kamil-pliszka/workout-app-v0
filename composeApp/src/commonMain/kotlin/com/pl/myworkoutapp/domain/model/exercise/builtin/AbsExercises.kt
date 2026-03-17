package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId


val ABS_EXERCISES = listOf(
    BuiltInExercise (
        id =  BuiltInExerciseId.RUSSIAN_TWIST.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS_PER_SIDE,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.FLUTTER_KICKS.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.REVERSE_CRUNCHES.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.HEEL_TOUCH.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS_PER_SIDE,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.DEAD_BUG.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS_PER_SIDE,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.BUTT_BRIDGE.asExerciseId(),
        muscle = MuscleGroup.ABS,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
    ),


)