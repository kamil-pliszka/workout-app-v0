package com.pl.myworkoutapp.ui.execution

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pl.myworkoutapp.ui.execution.components.*

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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "PAUSED: ${state.workoutId}"
                )
            }
            PausedView(/*state, viewModel*/)
        }
        is WorkoutExecutionState.Finished -> {
            //TODO - niepotrzebne przejscie stanu przez Composable
            onAction(WorkoutExecutionAction.OnExit)
        }
    }
}