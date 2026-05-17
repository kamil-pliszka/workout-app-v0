package com.pl.myworkoutapp.ui.execution.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionAction
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionUiState
import com.pl.myworkoutapp.ui.theme.AppTheme

@Composable
fun FinishedView(
    state: WorkoutExecutionUiState.Finished,
    onAction: (WorkoutExecutionAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxWidth().height(4.dp)
            )
            Text("FinishedView")
            Text("Workout finished")

            Text(state.title ?: "")

            Button(
                onClick = {
                    onAction(
                        WorkoutExecutionAction.OnExit
                    )
                }
            ) {
                Text("Close")
            }
        }
    }
}

@Preview
@Composable
private fun FinishedViewPreview() {
    AppTheme {
        FinishedView(
            state = WorkoutExecutionUiState.Finished(
                title = "FinishedView",
            ),
            onAction = { }
        )
    }
}