package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.workouts.components.WorkoutHeaderCard
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

//NAV SCREEN
@Composable
fun WorkoutsScreen(
    state: WorkoutsUiState,
    onAction: (WorkoutsAction) -> Unit,
    //appNavigator: AppNavigator = koinInject(),
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
                        //appNavigator.navigateToWorkoutDetails(workoutUiModel.workoutId)
                        onAction(WorkoutsAction.ShowWorkoutDetails(workoutUiModel.workoutId))
                    }
                )
            }
        }
        FabButton(onAction = onAction)
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

@Composable
private fun BoxScope.FabButton(
    onAction: (WorkoutsAction) -> Unit,
) {
    // Stan menu
    var menuExpanded by remember { mutableStateOf(false) }

    // FAB z Menu
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = (-16).dp)
    ) {
        FloatingActionButton(
            onClick = { menuExpanded = true },
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = stringResource(Res.string.btn_add)
            )
        }

//        //tymczasoe, do szybkiego odpalenia edytora
//        FloatingActionButton(
//            onClick = {
//                appNavigator.navigateToExerciseEditor(ExerciseId.Custom.NEW)
//            },
//            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-16).dp),
//            shape = CircleShape
//        ) {
//            Icon(
//                painter = painterResource(Res.drawable.ic_add),
//                contentDescription = stringResource(Res.string.btn_create)
//            )
//        }

        // Menu wyboru
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.workouts_add_exercise)) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_exercise),
                        contentDescription = "add exe"
                    )
                },
                onClick = {
                    menuExpanded = false
                    onAction(WorkoutsAction.AddExercise)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.workouts_add_workout)) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_cycle),
                        contentDescription = "add workout"
                    )
                },
                onClick = {
                    menuExpanded = false
                    onAction(WorkoutsAction.AddWorkout)
                }
            )
        }
    }
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
        //appNavigator = AppNavigator()
    )
}