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
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.common.asUiImage
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.execution.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_squat

@Composable
fun PausedView(
    state: WorkoutExecutionUiState.Paused,
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
            Text("PausedView")
            Text(state.title.asString())

            state.currentExercise?.let {
                Text("${it.exerciseId}")
            }

            Button(
                onClick = {
                    onAction(
                        WorkoutExecutionAction.ResumeClicked
                    )
                }
            ) {
                Text("Resume")
            }
        }
    }
}

@Preview
@Composable
private fun PausedViewPreview() {
    AppTheme {
        PausedView(
            state = WorkoutExecutionUiState.Paused(
                title = "PausedView".asUiText(),
                currentExercise = UiExercise(
                    exerciseId = BuiltInExerciseId.V_HOLD.asExerciseId(),
                    title = "Super ćwiczenie".asUiText(),
                    image = Res.drawable.ic_squat.asUiImage(),
                    quantityLabel = "123".asUiText(),
                ),
                progress = 0.31f,
            ),
            onAction = { }
        )
    }
}