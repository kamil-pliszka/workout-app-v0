package com.pl.myworkoutapp.ui.execution

import androidx.compose.runtime.*
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.ui.execution.components.*
import com.pl.myworkoutapp.ui.exercises.ExerciseEditorAction
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import org.koin.compose.koinInject

@Composable
fun WorkoutExecutionScreen(
    state: WorkoutExecutionState,
    onAction: (WorkoutExecutionAction) -> Unit,
) {
    LaunchedEffect(Unit) {
        onAction(WorkoutExecutionAction.OnScreenEntered)
    }
    DisposableEffect(Unit) {
        onDispose {
            onAction(WorkoutExecutionAction.OnScreenExited)
        }
    }

    //WorkoutEffectsHandler(viewModel.effects)
    when (state) {
        is WorkoutExecutionState.Running -> {
            RunningWorkoutView(/*state, viewModel*/)
        }
        is WorkoutExecutionState.Rest -> {
            RestView(/*state, viewModel*/)
        }
        is WorkoutExecutionState.Paused -> {
            PausedView(/*state, viewModel*/)
        }
        is WorkoutExecutionState.Finished -> {
            //TODO - niepotrzebne przejscie stanu przez Composable
            onAction(WorkoutExecutionAction.OnExit)
        }
    }
}