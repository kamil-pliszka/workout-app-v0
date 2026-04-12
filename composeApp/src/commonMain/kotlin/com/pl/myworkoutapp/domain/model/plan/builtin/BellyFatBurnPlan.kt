package com.pl.myworkoutapp.domain.model.plan.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.plan.*
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId

val BellyFatBurnPlan = TrainingPlan(
    id = BuiltInTrainingPlanId.BELLY_FAT_BURN,
    difficulty = Difficulty.INTERMEDIATE,
    days = listOf(
        //1 tydzień
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_20_MIN.asWorkoutId()),
        RestDayItem,
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        RestDayItem,
        RestDayItem,
        //2 tydzięń
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_20_MIN.asWorkoutId()),
        RestDayItem,
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        WorkoutDayItem(BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId()),
        RestDayItem,
        RestDayItem,
    ).toTrainingDays()
)