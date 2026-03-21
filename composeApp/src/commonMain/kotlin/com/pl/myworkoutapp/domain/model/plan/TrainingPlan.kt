package com.pl.myworkoutapp.domain.model.plan

import com.pl.myworkoutapp.domain.model.Difficulty

//Training Plan / Program – plan na wiele dni (np. 30-dniowy)
data class TrainingPlan(
    val id: BuiltInTrainingPlanId,
    //val category: Category,
    val difficulty: Difficulty,
    val days: List<TrainingDay>,
    val durationDays: Int = days.size,
)