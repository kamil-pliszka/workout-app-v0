package com.pl.myworkoutapp.ui.execution.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.common.asUiImage
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.execution.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.theme.EurostileExt
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_push_up
import myworkoutapplication.composeapp.generated.resources.ic_squat

@Composable
fun ExerciseView(
    state: WorkoutExecutionUiState.Exercise,
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
            Text("ExerciseView")
            Text(state.title.asString())
            when(state.target) {
                is UiExerciseTarget.Distance -> {
                    Text("Dystans: " + state.target.meters)
                }
                is UiExerciseTarget.Duration -> {
                    Text(
                        text = "CZAS: ${state.target.remainingSeconds}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = EurostileExt,
                        fontSize = 32.sp,
                    )
                }
                is UiExerciseTarget.Reps -> {
                    Text("Powtórzenia: " + state.target.reps)
                }
            }
            Text("${state.currentExercise.exerciseId}")

            state.nextExercise?.let {
                Text("Next: ${it.exerciseId}")
            }

            Row {

                Button(
                    onClick = {
                        onAction(
                            WorkoutExecutionAction.FinishExerciseClicked
                        )
                    }
                ) {
                    Text("Finish")
                }

                Button(
                    onClick = {
                        onAction(
                            WorkoutExecutionAction.PauseClicked
                        )
                    }
                ) {
                    Text("Pause")
                }

                Button(
                    onClick = {
                        onAction(
                            WorkoutExecutionAction.SkipClicked
                        )
                    }
                ) {
                    Text("Skip")
                }
            }
        }
    }
}


@Preview
@Composable
private fun ExerciseViewPreviewReps() {
    AppTheme {
        ExerciseView(
            state = WorkoutExecutionUiState.Exercise(
                title = "ExerciseView".asUiText(),
                nextExercise = UiExercise(
                    exerciseId = BuiltInExerciseId.DEAD_BUG.asExerciseId(),
                    title = "Super ćwiczenie".asUiText(),
                    image = Res.drawable.ic_squat.asUiImage(),
                    quantityLabel = "123".asUiText(),
                ),
                progress = 0.31f,
                currentExercise = UiExercise(
                    exerciseId = BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
                    title = "Pompujesz".asUiText(),
                    image = Res.drawable.ic_push_up.asUiImage(),
                    quantityLabel = "37 albo i lepiej".asUiText(),
                ),
                target = UiExerciseTarget.Reps(31),
                canPause = true,
                canSkip = true,
            ),
            onAction = { }
        )
    }
}