package com.pl.myworkoutapp.ui.execution

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import com.pl.myworkoutapp.ui.execution.components.*

//NAV SCREEN
//WorkoutExecutionScreen
//powinien być:
//root orchestration composable
//phase router
@Composable
fun WorkoutExecutionScreen(
    state: WorkoutExecutionUiState,
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
    when(state) {
        WorkoutExecutionUiState.Loading -> {
            CircularProgressIndicator()
        }
        is WorkoutExecutionUiState.Intro -> {
            IntroView(
                state = state,
                onAction = onAction,
            )
        }
        is WorkoutExecutionUiState.Rest -> {
            RestView(
                state = state,
                onAction = onAction,
            )
        }
        is WorkoutExecutionUiState.Exercise -> {
            ExerciseView(
                state = state,
                onAction = onAction,
            )
        }
        is WorkoutExecutionUiState.Paused -> {
            PausedView(
                state = state,
                onAction = onAction,
            )
        }
        is WorkoutExecutionUiState.Finished -> {
            FinishedView(
                state = state,
                onAction = onAction,
            )
        }
    }
}