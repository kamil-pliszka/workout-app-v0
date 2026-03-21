package com.pl.myworkoutapp.ui.plans

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.plans.components.PlanCard
import com.pl.myworkoutapp.ui.theme.TurquoiseBlue
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_plank1

@Composable
fun PlansScreen(
    state: PlansUiState,
    onAction: (PlansAction) -> Unit,
) {
//    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
//        Text("Plans")
//        TextButton(onClick = { onAction(PlansAction.NavToWorkout(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId())) }) {
//            Text("Navigate TO workout exec")
//        }
//    }

    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.plans.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onAction(PlansAction.OnPageChanged(pagerState.currentPage))
    }

    HorizontalPager(state = pagerState) { page ->
        val plan = state.plans[page]

        PlanCard(
            plan = plan,
            onStartClick = { dayIndex ->
                println("dayIndex = $dayIndex")
                onAction(PlansAction.OnStartPlan(plan.id))
            }
        )
    }
}

val PREVIEW_PLAN = PlanUiModel(
    id = "test",
    name = "Najlepsiejszy plan treningowy.".asUiText(),
    desc = "Krótki opis planu trengowego".asUiText(),
    imageUrl = Res.drawable.ic_plank1,
    days = List(10) { idx ->
        val isRestDay = idx % 3 == 2
        PlanDayUiModel(
            desc = "${(idx*171+13)%37} ćwiczeń / ${((idx+56)*14%43)} min / ${((idx + 6) * 131 % 431)} kcal".asUiText(),//TODO
            dayIndex = idx + 1,
            dayType = if (isRestDay) DayType.Rest else DayType.Workout,
            dayProgress = when {
                idx < 4 -> DayProgress.Done
                idx == 4 -> DayProgress.InProgress(0.3f)
                else -> DayProgress.NotStarted
            },
            isCurrent = idx == 4
        )
    },
    isInProgress = true,
    difficulty = Difficulty.ADVANCED,
    themeColor = TurquoiseBlue,
)

val PREVIEW_UISTATE = PlansUiState(
    isLoading = false,
    plans = listOf(
        PREVIEW_PLAN,
        PREVIEW_PLAN,
        PREVIEW_PLAN
    ),
    currentPage = 0,
)

@Preview
@Composable
fun PlansScreenPreview() {
    PlansScreen(
        state = PREVIEW_UISTATE,
        onAction = {}
    )
}
