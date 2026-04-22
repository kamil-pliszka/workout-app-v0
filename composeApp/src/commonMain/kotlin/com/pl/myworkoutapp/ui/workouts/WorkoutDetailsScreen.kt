package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.ui.exercises.ExercisePickerScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesComponent
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun WorkoutDetailsScreen(
    state: WorkoutDetailsUiState,
    onAction: (WorkoutDetailsAction) -> Unit,
    onEditorAction: (WorkoutEditorAction) -> Unit,
    onCircuitEditorAction: (CircuitEditorAction) -> Unit,
) {
    if (state.isLoading || state.workout == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.editableWorkout != null) {
        WorkoutEditorScreen(
            state = state.editableWorkout,
            onAction = onAction,
            onEditorAction = onEditorAction,
            onCircuitEditorAction = onCircuitEditorAction,
        )
    } else {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                //verticalArrangement = Arrangement.Center,
                //horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WorkoutWithExercisesComponent(
                    workoutUiModel = state.workout,
                    onAction = { onAction(it.toWorkoutDetailsAction()) },
                )
            }
            WorkoutDetailBottomButtons(
                state = state,
                onAction = onAction
            )
        }
    }


    if (state.exerciseInfo != null) {
        WorkoutExerciseInfoScreen(
            exerciseInfo = state.exerciseInfo,
            onAction = { onAction(it.toWorkoutDetailsAction()) }
        )
    }
    if (state.showExercisePicker) {
        ExercisePickerScreen(
            currentExerciseId = state.exerciseInfo?.exerciseId,
            onResult = { exerciseId -> onAction(WorkoutDetailsAction.ExercisePicked(exerciseId))},
        )
    }
}

@Composable
private fun BoxScope.WorkoutDetailBottomButtons(
    state: WorkoutDetailsUiState,
    onAction: (WorkoutDetailsAction) -> Unit,
) {
    //pasek przyciskow na dole
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)
    ) {
        if (state.isDirty) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton( // Use Outlined for secondary action
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(WorkoutDetailsAction.OnResetWorkout) },
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_reset_settings), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.workout_reset))
                }
                Button( // Filled for primary action
                    modifier = Modifier.weight(1f),
                    onClick = { onAction(WorkoutDetailsAction.OnSaveWorkout) }
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_check), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.btn_save))
                }
            }
        } else {
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
}

@Preview
@Composable
private fun WorkoutDetailsScreenPreviewEN() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET)
    val workoutUiModel = transform(workout)
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = workoutUiModel,
            isDirty = true,
        ),
        onAction = { },
        onEditorAction = { },
        onCircuitEditorAction = { },
    )
}

@Preview(locale = "pl")
@Composable
private fun WorkoutDetailsScreenPreviewPL() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    val workoutUiModel = transform(workout)
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = workoutUiModel
        ),
        onAction = { },
        onEditorAction = { },
        onCircuitEditorAction = { },
    )
}
