package com.pl.myworkoutapp.domain.model.plan.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlanId
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan

val SixPackAbsPlan = TrainingPlan(
    id = BuiltInTrainingPlanId.SIX_PACK_ABS,
    difficulty = Difficulty.ADVANCED,
    days = listOf(
        //TODO
    )
)
