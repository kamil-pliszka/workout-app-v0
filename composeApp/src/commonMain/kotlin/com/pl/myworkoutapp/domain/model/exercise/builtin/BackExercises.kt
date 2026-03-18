package com.pl.myworkoutapp.domain.model.exercise.builtin

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExercise
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.domain.model.exercise.ExerciseType
import com.pl.myworkoutapp.domain.model.exercise.MuscleGroup
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId


val BACK_EXERCISES = listOf(
    BuiltInExercise (
        id =  BuiltInExerciseId.COBRA_STRETCH.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRETCH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.SUPERMAN.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.REPS,
    ),

    BuiltInExercise (
        id =  BuiltInExerciseId.V_HOLD.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRENGTH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),


    BuiltInExercise (
        id =  BuiltInExerciseId.CHILD_POSE.asExerciseId(),
        muscle = MuscleGroup.BACK,
        exerciseType = ExerciseType.STRETCH,
        equipment = Equipment.BODYWEIGHT,
        met = 1.0,
        quantityType = QuantityType.DURATION,
    ),
)