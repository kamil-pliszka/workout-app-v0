package com.pl.myworkoutapp.ui.plans

import com.pl.myworkoutapp.domain.model.plan.RestDayItem
import com.pl.myworkoutapp.domain.model.plan.TrainingPlan
import com.pl.myworkoutapp.ui.common.asUiText

fun TrainingPlan.toUi(): PlanUiModel {
    val config = id.toUiConfig()
    return PlanUiModel(
        id = id.name,
        name = config.name,
        desc = config.desc,
        imageUrl = config.image,
        themeColor = config.color,
        days = days.mapIndexed { index, day ->
            PlanDayUiModel(
                desc = "TODO-desc".asUiText(),//TODO
                dayIndex = index + 1,
                dayType = if (day.item is RestDayItem) DayType.Rest else DayType.Workout,
                dayProgress = DayProgress.NotStarted,//TODO
                isCurrent = false,//TODO
            )
        },
        //todo : isInProgress = days.any { it.isCurrent }
        isInProgress = true,
        difficulty = difficulty,
    )
}


