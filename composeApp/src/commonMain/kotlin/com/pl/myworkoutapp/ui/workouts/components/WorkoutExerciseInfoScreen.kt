package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface WorkoutExerciseInfoAction {
    object ShowExercisePicker : WorkoutExerciseInfoAction
    data class ChangeQuantity(val increase: Boolean) : WorkoutExerciseInfoAction
    object ExercisePrev: WorkoutExerciseInfoAction
    object ExerciseNext: WorkoutExerciseInfoAction
    object ExerciseReset: WorkoutExerciseInfoAction
    object ExerciseSave: WorkoutExerciseInfoAction
    object CloseExerciseInfo: WorkoutExerciseInfoAction
}

@Composable
fun WorkoutExerciseInfoScreen(
    exerciseInfo: ExerciseInfoUiModel,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
) {
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true // od razu full
//    )
//    ModalBottomSheet(
//        onDismissRequest = { onAction(WorkoutExerciseInfoAction.CloseExerciseInfo) },
//        sheetState = sheetState,
//        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//        //dragHandle = null // opcjonalnie ukrycie kreski
//    ) {
//        WorkoutExerciseInfoContent(
//            exerciseInfo = exerciseInfo,
//            onAction = onAction,
//        )
//    }
    WorkoutExerciseInfoContent(
        exerciseInfo = exerciseInfo,
        onAction = onAction,
    )
}


@Composable
private fun WorkoutExerciseInfoContent(
    exerciseInfo: ExerciseInfoUiModel,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { /* consume click */ }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            Spacer(Modifier.height(8.dp))
            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "", //""V CRUNCH",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = { onAction(WorkoutExerciseInfoAction.ShowExercisePicker) }) {
                    Text(stringResource(Res.string.workout_change))
                }
            }

           Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    //.height(IntrinsicSize.Min)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))
                //CONTENT
                ExerciseInfoComponent(
                    exerciseInfo = exerciseInfo,
                    changeQtyButtons = true,
                    quantityChangeAction = { increase ->
                        onAction(
                            WorkoutExerciseInfoAction.ChangeQuantity(
                                increase
                            )
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    //.align(Alignment.BottomCenter)
                    //.background(Color.Red)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BottomButtons(
                    exerciseInfo = exerciseInfo,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
fun NextPrev(
    current: Int,
    total: Int,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
) {
    // LEWA CZĘŚĆ (strzałki + licznik)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            //.background(Color.LightGray)
            .background(MaterialTheme.colorScheme.primaryContainer)
        //.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = { onAction(WorkoutExerciseInfoAction.ExercisePrev)},
            enabled = current > 1,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_skip_previous),
                contentDescription = "Prev"
            )
        }

        Text("$current/$total")

        IconButton(
            onClick = { onAction(WorkoutExerciseInfoAction.ExerciseNext)},
            enabled = current < total
        ) {
            Icon(painter = painterResource(Res.drawable.ic_skip_next), contentDescription = "Next")
        }
    }
}

@Composable
private fun RowScope.BottomButtons(
    exerciseInfo: ExerciseInfoUiModel,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
) {
    //NEXT//PREV // RESET
    Box(modifier = Modifier.weight(4f)) {
        if (exerciseInfo.quantityDirty) {
            Button(
                onClick = { onAction(WorkoutExerciseInfoAction.ExerciseReset)},
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painter = painterResource(Res.drawable.ic_reset_settings), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.btn_reset))
            }
        } else {
            NextPrev(
                current = exerciseInfo.current ?: 0,
                total = exerciseInfo.total ?: 0,
                onAction = onAction,
            )
        }
    }
    Box(modifier = Modifier.weight(6f)) {
        // 🔒 STICKY BUTTON
        if (exerciseInfo.quantityDirty) {
            Button(
                onClick = { onAction(WorkoutExerciseInfoAction.ExerciseSave) },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(stringResource(Res.string.workout_qty_save))
            }
        } else {
            Button(
                onClick = { onAction(WorkoutExerciseInfoAction.CloseExerciseInfo) },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(stringResource(Res.string.workout_close))
            }
        }
    }
}


val EXE_FK = BuiltInExerciseRegistry.get(BuiltInExerciseId.FLUTTER_KICKS)


@Preview
@Composable
private fun ExerciseBottomSheet() {
    AppTheme {
        val exerciseInfo = EXE_FK.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        WorkoutExerciseInfoContent(
            exerciseInfo = exerciseInfo.copy(
                quantityValue = 17,
                descriptionMarkdown = markdown
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ExerciseBottomSheetDirty() {
    AppTheme {
        val exerciseInfo = EXE_FK.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        WorkoutExerciseInfoContent(
            exerciseInfo = exerciseInfo.copy(
                quantityValue = 17,
                descriptionMarkdown = markdown,
                quantityDirty = true,
            ),
            onAction = {},
        )
    }
}