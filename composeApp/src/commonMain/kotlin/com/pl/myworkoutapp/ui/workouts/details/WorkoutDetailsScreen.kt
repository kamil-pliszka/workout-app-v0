package com.pl.myworkoutapp.ui.workouts.details

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
import com.pl.myworkoutapp.ui.common.ConfirmationDialog
import com.pl.myworkoutapp.ui.exercises.ExercisePickerScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutExerciseInfoScreen
import com.pl.myworkoutapp.ui.workouts.components.WorkoutWithExercisesComponent
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewAction.DeleteCancel
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewAction.DeleteConfirm
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewAction.ExercisePicked
import com.pl.myworkoutapp.ui.workouts.tree.transform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


//NAV SCREEN
@Composable
fun WorkoutDetailsScreen(
    state: WorkoutDetailsUiState,
    events: Flow<WorkoutEditorEvent>,
    onViewAction: (WorkoutViewAction) -> Unit,
    onEditAction: (WorkoutEditAction) -> Unit,
    onCircuitEditorAction: (CircuitEditorAction) -> Unit,
) {
    if (state.mode is WorkoutDetailsMode.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.mode is WorkoutDetailsMode.Edit) {
        WorkoutEditorScreen(
            state = state.mode.session,
            events = events,
            onEditAction = onEditAction,
            onCircuitEditorAction = onCircuitEditorAction,
        )
        state.mode.session.activeExercise?.let { exeSession ->
            WorkoutExerciseInfoScreen(
                exerciseInfo = exeSession.draft,
                onAction = { onEditAction(it.toWorkoutEditAction()) }
            )
        }
        if (state.mode.session.modal is WorkoutEditModal.ExercisePicker) {
            ExercisePickerScreen(
                currentExerciseId = state.mode.session.modal.currentExerciseId,
                onResult = { exerciseId -> onEditAction(WorkoutEditAction.ExercisePicked(exerciseId)) },
            )
        }
    }

    if (state.mode is WorkoutDetailsMode.View) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                //verticalArrangement = Arrangement.Center,
                //horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WorkoutWithExercisesComponent(
                    workoutUiModel = state.mode.session.workout,
                    onAction = { onViewAction(it.toWorkoutViewAction()) },
                )
            }
            WorkoutDetailBottomButtons(
                state = state.mode.session,
                onViewAction = onViewAction
            )
        }
        state.mode.session.activeExercise?.let { exeSession ->
            WorkoutExerciseInfoScreen(
                exerciseInfo = exeSession.draft,
                onAction = { onViewAction(it.toWorkoutViewAction()) }
            )
        }

        when (state.mode.session.modal) {
            is WorkoutViewModal.ExercisePicker ->
                ExercisePickerScreen(
                    currentExerciseId = state.mode.session.activeExercise?.draft?.exerciseId,
                    onResult = { exerciseId -> onViewAction(ExercisePicked(exerciseId)) },
                )

            is WorkoutViewModal.ConfirmReset -> {
                //obydwa przebiegi: reset/delete robią to samo:
                //usuń workout i wróć do fallbacku / zamknij ekran
                //jedyna różnica jest z punktu widzenia usera, bo widzi inne buttony akcji
                //UI wysyła jedną intencję: “usuń workout”
                ConfirmationDialog(
                    title = stringResource(Res.string.workout_view_reset_title),
                    text = stringResource(Res.string.workout_view_reset_question),
                    onConfirm = {
                        onViewAction(DeleteConfirm)
                    },
                    confirmText = stringResource(Res.string.btn_reset),
                    confirmButtonColors = buttonColors(MaterialTheme.colorScheme.error),
                    onCancel = {
                        onViewAction(DeleteCancel)
                    },
                )
            }

            is WorkoutViewModal.ConfirmDelete -> {
                ConfirmationDialog(
                    title = stringResource(Res.string.workout_view_delete_title),
                    text = stringResource(Res.string.workout_view_delete_question),
                    onConfirm = {
                        //patrz komentarz do: is WorkoutViewModal.ConfirmReset
                        onViewAction(DeleteConfirm)
                    },
                    confirmText = stringResource(Res.string.btn_delete),
                    confirmButtonColors = buttonColors(MaterialTheme.colorScheme.error),
                    onCancel = {
                        onViewAction(DeleteCancel)
                    },
                )
            }

            null -> Unit
        }
    }
}

@Composable
private fun BoxScope.WorkoutDetailBottomButtons(
    state: WorkoutViewSession,
    onViewAction: (WorkoutViewAction) -> Unit,
) {
    //pasek przyciskow na dole
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)
    ) {
        if (state.hasUnsavedChanges) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    // Use Outlined for secondary action
                    modifier = Modifier.weight(1f),
                    onClick = { onViewAction(WorkoutViewAction.ResetWorkout) },
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_reset_settings),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.workout_reset))
                }
                Button( // Filled for primary action
                    modifier = Modifier.weight(1f),
                    onClick = { onViewAction(WorkoutViewAction.SaveWorkout) }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.btn_save))
                }
            }
        } else {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onViewAction(WorkoutViewAction.StartWorkout)
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
            mode = WorkoutDetailsMode.View(
                session = WorkoutViewSession(
                    workout = workoutUiModel,
                    hasUnsavedChanges = true
                )
            ),
        ),
        events = emptyFlow(),
        onViewAction = { },
        onEditAction = { },
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
            mode = WorkoutDetailsMode.View(
                session = WorkoutViewSession(
                    workout = workoutUiModel,
                )
            ),
        ),
        events = emptyFlow(),
        onViewAction = { },
        onEditAction = { },
        onCircuitEditorAction = { },
    )
}
