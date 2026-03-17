package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId

val CORE_EXERCISES = listOf(
    BuiltInExercise(
        id = BuiltInExerciseId.PLANK.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 3.5,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_PLANK_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_PLANK_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.BENT_LEG_TWIST.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.CARDIO,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS_PER_SIDE,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_CRUNCHES_LEFT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.HOLLOW_BODY.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (//czy to rzeczywiście będzie potrzebne?
        id =  BuiltInExerciseId.TABATA.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.MOBILITY,
        equipment = Equipment.BODYWEIGHT,
        met = 2.5,
        quantityType = QuantityType.DURATION,
    ),


    BuiltInExercise (
        id =  BuiltInExerciseId.PUSH_UP_HOLD_DOWN.asExerciseId(),
        muscle = MuscleGroup.CORE,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),


)