package com.pl.myworkoutapp.ui.plans.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.plans.DayProgress
import com.pl.myworkoutapp.ui.plans.DayType
import com.pl.myworkoutapp.ui.plans.PlanDayUiModel
import com.pl.myworkoutapp.ui.plans.PlanUiModel
import com.pl.myworkoutapp.ui.theme.TrafficPurple
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_plank1

@Composable
fun PlanCard(
    plan: PlanUiModel,
    onStartClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PlanHeader(plan)

        Spacer(Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //items(plan.days)
            plan.days.forEach { day ->
                DayCard(
                    day = day,
                    themeColor = plan.themeColor,
                    onStartClick = { onStartClick(day.dayIndex) }
                )
            }
        }
    }
}


val PREVIEW_CARD_PLAN = PlanUiModel(
    id = "test",
    name = "Najlepsiejszy plan treningowy".asUiText(),
    desc = "Dużo wyzwań i odpoczynku ;)\nktóż tego nie lubi".asUiText(),
    //imageUrl = Res.drawable.ic_rest_day1,
    imageUrl = Res.drawable.ic_plank1,
    days = List(10) { idx ->
        val isRestDay = idx % 3 == 2
        PlanDayUiModel(
            dayIndex = idx + 1,
            dayType = if (isRestDay) DayType.Rest else DayType.Workout,
            dayProgress = when {
                idx < 4 -> DayProgress.Done
                idx == 4 -> DayProgress.InProgress(0.3f)
                else -> DayProgress.NotStarted
            },
            isCurrent = idx == 4,
            desc = (if (isRestDay)
                "Dzień odpoczynku"
            else "${(idx * 71 + 113) % 31} ćwiczeń / ${((idx + 15) * 13 % 41)} min / ${((idx + 16) * 113 % 413)} kcal"
                    ).asUiText(),
        )
    },
    isInProgress = true,
    difficulty = Difficulty.BEGINNER,
    themeColor = TrafficPurple,
)

@Preview
@Composable
fun PlanCardPreview() {
    PlanCard(PREVIEW_CARD_PLAN, {})
}
