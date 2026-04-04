package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoBottomSheet
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesComponent
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.workout_start
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutDetailsScreen(
    state: WorkoutDetailsUiState,
    onAction: (WorkoutDetailsAction) -> Unit,
) {
    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    if (state.workout == null) {
        Text("empty")
        return
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            WorkoutWithExercisesComponent(
                workoutUiModel = state.workout,
                onExerciseClick = { workoutUiItem ->
                    onAction(WorkoutDetailsAction.ShowExerciseInfo(workoutUiItem))
                }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(WorkoutDetailsAction.OnStartWorkout)
                }
            ) {
                Text(stringResource(Res.string.workout_start))
            }
        }
    }

    if (state.exerciseInfo != null) {
        WorkoutExerciseInfoBottomSheet(
            exerciseInfo = state.exerciseInfo,
            onDismiss = { onAction(WorkoutDetailsAction.CloseExerciseInfo) },
            onChangeExercise = { onAction(WorkoutDetailsAction.ChangeExercise) },
            onChangeQuantity = { increase -> onAction(WorkoutDetailsAction.ChangeQuantity(increase)) },
            onQuantitySave = { onAction(WorkoutDetailsAction.QuantitySave) },
        )
    }
}

@Preview
@Composable
private fun WorkoutDetailsScreenPreviewEN() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = transform(workout)
        ),
        onAction = { }
    )
}

@Preview(locale = "pl")
@Composable
private fun WorkoutDetailsScreenPreviewPL() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = transform(workout)
        ),
        onAction = { }
    )
}
