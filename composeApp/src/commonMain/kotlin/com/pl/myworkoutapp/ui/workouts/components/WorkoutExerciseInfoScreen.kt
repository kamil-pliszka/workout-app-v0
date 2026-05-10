package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.ui.components.BaseOverlayScreen
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface WorkoutExerciseInfoAction {
    data class ChangeQuantity(val increase: Boolean) : WorkoutExerciseInfoAction
    object ExercisePrev : WorkoutExerciseInfoAction
    object ExerciseNext : WorkoutExerciseInfoAction
    object ExerciseReset : WorkoutExerciseInfoAction
    object ExerciseSave : WorkoutExerciseInfoAction
    object CloseExerciseInfo : WorkoutExerciseInfoAction
}

//OVERLAY SCREEN
@Composable
fun WorkoutExerciseInfoScreen(
    exerciseInfo: ExerciseInfoUiModel,
    showExchangeButton: Boolean = true,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
    onExchangeExerciseRequest: () -> Unit,
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
        showExchangeButton = showExchangeButton,
        onAction = onAction,
        onExchangeExerciseRequest = onExchangeExerciseRequest
    )
}


@Composable
private fun WorkoutExerciseInfoContent(
    exerciseInfo: ExerciseInfoUiModel,
    showExchangeButton: Boolean,
    onAction: (WorkoutExerciseInfoAction) -> Unit,
    onExchangeExerciseRequest: () -> Unit,
) {
    BaseOverlayScreen(
        headerContent = {
            Text(
                text = "", //""V CRUNCH",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            if (showExchangeButton) {
                TextButton(onClick = onExchangeExerciseRequest) {
                    Text(stringResource(Res.string.workout_change))
                }
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.weight(1f)
                    //.height(IntrinsicSize.Min)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(16.dp))
                //CONTENT
                ExerciseInfoComponent(
                    exerciseInfo = exerciseInfo,
                    showQuantity = true,
                    quantityChangeAction = { increase ->
                        onAction(
                            WorkoutExerciseInfoAction.ChangeQuantity(
                                increase
                            )
                        )
                    }
                )
            }
        },
        bottomContent = {
            BottomButtons(
                exerciseInfo = exerciseInfo,
                onAction = onAction,
            )
        },
        maxHeight = 0.95f,
        onCancel = { }
    )
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
            .background(MaterialTheme.colorScheme.primaryContainer)
        //.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = { onAction(WorkoutExerciseInfoAction.ExercisePrev) },
            enabled = current > 1,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_skip_previous),
                contentDescription = "Prev"
            )
        }

        Text("$current/$total")

        IconButton(
            onClick = { onAction(WorkoutExerciseInfoAction.ExerciseNext) },
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
        if (exerciseInfo.isDirty) {
            Button(
                onClick = { onAction(WorkoutExerciseInfoAction.ExerciseReset) },
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_settings),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.btn_reset))
            }
        } else {
            NextPrev(
                current = exerciseInfo.position ?: 0,
                total = exerciseInfo.total ?: 0,
                onAction = onAction,
            )
        }
    }
    Box(modifier = Modifier.weight(6f)) {
        // 🔒 STICKY BUTTON
        if (exerciseInfo.isDirty) {
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
            showExchangeButton = true,
            onAction = {},
            onExchangeExerciseRequest = { },
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
                isDirty = true,
            ),
            showExchangeButton = false,
            onAction = { },
            onExchangeExerciseRequest = { },
        )
    }
}