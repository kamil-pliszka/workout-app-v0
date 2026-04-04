package com.pl.myworkoutapp.ui.execution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.ui.execution.components.PausedView
import com.pl.myworkoutapp.ui.execution.components.RestView
import com.pl.myworkoutapp.ui.execution.components.RunningWorkoutView
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import org.koin.compose.koinInject

@Composable
fun WorkoutExecutionScreen(
    state: WorkoutExecutionState,
    appStateHolder: AppStateHolder = koinInject(),
    onFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        appStateHolder.setWorkoutActive(true)
        appStateHolder.setThemeColor(PearlOpalGreen)
    }
    DisposableEffect(Unit) {
        onDispose {
            appStateHolder.setThemeColor(null)
            appStateHolder.setWorkoutActive(false)
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
            onFinish()
        }
    }
}