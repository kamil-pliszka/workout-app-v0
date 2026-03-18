package com.pl.myworkoutapp.ui.execution

import androidx.compose.runtime.Composable
import com.pl.myworkoutapp.ui.execution.components.PausedView
import com.pl.myworkoutapp.ui.execution.components.RestView
import com.pl.myworkoutapp.ui.execution.components.RunningWorkoutView

@Composable
fun WorkoutExecutionScreen(
    state: WorkoutExecutionState,
    onFinish: () -> Unit
) {
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