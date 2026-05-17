package com.pl.myworkoutapp.ui.execution.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.execution.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.theme.EurostileExt

@Composable
fun RestView(
    state: WorkoutExecutionUiState.Rest,
    onAction: (WorkoutExecutionAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier.fillMaxWidth().height(4.dp)
            )
            Text("RestView")
            Text(state.title ?: "REST")

            Text(
                text = "CZAS: ${state.remainingSeconds}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = EurostileExt,
                fontSize = 32.sp,
            )

            state.nextExercise?.let {
                Text("Next: ${it.exerciseId}")
            }

            Button(
                onClick = {
                    onAction(WorkoutExecutionAction.SkipClicked)
                }
            ) {
                Text("Skip Rest")
            }

            Button(
                onClick = {
                    onAction(WorkoutExecutionAction.PauseClicked)
                }
            ) {
                Text("Pause")
            }
        }
    }
}


@Preview
@Composable
private fun RestViewPreview() {
    AppTheme {
        RestView(
            state = WorkoutExecutionUiState.Rest(
                title = "RestView",
                nextExercise = UiExercise(
                    exerciseId = BuiltInExerciseId.V_HOLD.asExerciseId()
                ),
                progress = 0.31f,
                remainingSeconds = 7,
                canPause = true,
                canSkip = true
            ),
            onAction = { }
        )
    }
}