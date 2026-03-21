package com.pl.myworkoutapp.domain.model.plan.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlanId
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan


val RockSolidAbsPlan = TrainingPlan(
    id = BuiltInTrainingPlanId.ROCK_SOLID_ABS,
    difficulty = Difficulty.ADVANCED,
    days = listOf(
        //TODO
    )
)