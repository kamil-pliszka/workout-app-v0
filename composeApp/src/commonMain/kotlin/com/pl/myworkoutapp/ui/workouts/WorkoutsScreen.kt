package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.workouts.components.WorkoutHeaderCard
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun WorkoutsScreen(
    state: WorkoutsUiState,
    onAction: (WorkoutsAction) -> Unit,
    appNavigator: AppNavigator = koinInject(),
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                        appNavigator.navigateToWorkoutDetails(workoutUiModel.workoutId)
                    }
                )
            }
        }

        //tymczasoe, do szybkiego odpalenia edytora
        FloatingActionButton(
            onClick = {
                appNavigator.navigateToExerciseEditor(ExerciseId.Custom.NEW)
            },
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-16).dp),
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.btn_create)
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
        onAction = { },
        appNavigator = AppNavigator()
    )
}