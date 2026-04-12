package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutRegistry
import com.pl.myworkoutapp.ui.exercises.ExercisePickerScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesComponent
import kotlinx.coroutines.runBlocking
import myworkoutapplication.composeapp.generated.resources.*
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
            if (state.isDirty) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                        onClick = {
                            onAction(WorkoutDetailsAction.OnResetWorkout)
                        }
                    ) {
                        Text(stringResource(Res.string.workout_reset))
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onAction(WorkoutDetailsAction.OnSaveWorkout)
                        }
                    ) {
                        Text(stringResource(Res.string.workout_save))
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

    if (state.exerciseInfo != null) {
        WorkoutExerciseInfoScreen(
            exerciseInfo = state.exerciseInfo,
            onAction = onAction
        )
    }
    if (state.showExercisePicker) {
        ExercisePickerScreen(
            currentExerciseId = state.exerciseInfo?.exerciseId,
            onResult = { exerciseId -> onAction(WorkoutDetailsAction.ExercisePicked(exerciseId))},
        )
    }
}

@Preview
@Composable
private fun WorkoutDetailsScreenPreviewEN() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    val workoutUiModel = runBlocking {
        transform(workout) { exerciseId ->
            BuiltInExerciseRegistry.get((exerciseId as ExerciseId.BuiltIn).id)
        }
    }
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = workoutUiModel,
            isDirty = true,
        ),
        onAction = { }
    )
}

@Preview(locale = "pl")
@Composable
private fun WorkoutDetailsScreenPreviewPL() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    val workoutUiModel = runBlocking {
        transform(workout) { exerciseId ->
            BuiltInExerciseRegistry.get((exerciseId as ExerciseId.BuiltIn).id)
        }
    }
    WorkoutDetailsScreen(
        state = WorkoutDetailsUiState(
            isLoading = false,
            workout = workoutUiModel
        ),
        onAction = { }
    )
}
