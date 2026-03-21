package com.pl.myworkoutapp.domain.model.plan

import com.pl.myworkoutapp.domain.model.workout.WorkoutId


data class TrainingDay(
    val day: Int,
    val item: TrainingPlanItem
)

//pojedyńczy element platnu treningowego
sealed interface TrainingPlanItem

data object RestDayItem : TrainingPlanItem

data class WorkoutDayItem(
    val workoutId: WorkoutId,
): TrainingPlanItem

fun List<TrainingPlanItem>.toTrainingDays() = this.mapIndexed { index, item -> TrainingDay(index + 1, item) }