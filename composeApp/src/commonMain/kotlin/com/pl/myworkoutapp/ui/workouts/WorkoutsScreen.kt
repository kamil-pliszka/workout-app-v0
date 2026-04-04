package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.domain.model.workout.asString
import com.pl.myworkoutapp.ui.workouts.components.WorkoutHeaderCard

@Composable
fun WorkoutsScreen(
    state: WorkoutsUiState,
    onAction: (WorkoutsAction) -> Unit,
) {
    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.workouts.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onAction(WorkoutsAction.OnPageChanged(pagerState.currentPage))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = state.workouts.size,
            key = { idx -> state.workouts[idx].workoutId.asString() }
        ) { idx ->
            val workoutUiModel = state.workouts[idx]
            WorkoutHeaderCard(
                workout = workoutUiModel,
                onClick = {
                    onAction(WorkoutsAction.NavToWorkout(workoutUiModel.workoutId))
                }
            )
        }
    }

    /*
    HorizontalPager(state = pagerState) { page ->
        val workout = state.workouts[page]
        WorkoutWithExercisesComponent(
            workout = workout,
            onExerciseClick = { workoutUiItem ->
                println("workoutUiItem = $workoutUiItem")
                //onAction(WorkoutsAction.OnStartPlan(workout.id))
            }
        )
    }
    */
}

@Preview
@Composable
private fun WorkoutsScreenPreview() {
    WorkoutsScreen(
        state = WorkoutsUiState(
            isLoading = false,
            workouts = listOf(
                BuiltInWorkoutRegistry.get(BuiltInWorkoutId.SIX_PACK_10_MIN),
                BuiltInWorkoutRegistry.get(BuiltInWorkoutId.SIX_PACK_20_MIN),
                BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET),
                BuiltInWorkoutRegistry.get(BuiltInWorkoutId.TABATA_1),
            ).map {
                it.toUi()
            },
            currentPage = 0,
        ),
        onAction = { }
    )
}