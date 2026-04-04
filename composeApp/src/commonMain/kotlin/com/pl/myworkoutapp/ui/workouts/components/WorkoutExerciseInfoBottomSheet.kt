package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.ui.exercises.ExerciseInfoComponent
import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel
import com.pl.myworkoutapp.ui.exercises.loadExerciseMarkdownForPreview
import com.pl.myworkoutapp.ui.exercises.toUi
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.workout_change
import myworkoutapplication.composeapp.generated.resources.workout_close
import myworkoutapplication.composeapp.generated.resources.workout_qty_save
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutExerciseInfoBottomSheet(
    exerciseInfo: ExerciseInfoUiModel,
    onDismiss: () -> Unit,
    onChangeExercise: () -> Unit,
    onChangeQuantity: (increase: Boolean) -> Unit,
    onQuantitySave: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // od razu full
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        //dragHandle = null // opcjonalnie ukrycie kreski
    ) {
        WorkoutExerciseInfoContent(
            exerciseInfo = exerciseInfo,
            onDismiss = onDismiss,
            onChangeExercise = onChangeExercise,
            onChangeQuantity = onChangeQuantity,
            onQuantitySave = onQuantitySave,
        )
    }
}


@Composable
private fun WorkoutExerciseInfoContent(
    exerciseInfo: ExerciseInfoUiModel,
    onDismiss: () -> Unit,
    onChangeExercise: () -> Unit,
    onChangeQuantity: (increase: Boolean) -> Unit,
    onQuantitySave: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            //.fillMaxHeight(0.90f) // prawie full screen
    ) {
        // 🔽 SCROLL CONTENT
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 0.dp)
                .padding(bottom = 100.dp) // miejsce na sticky button
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

                TextButton(onClick = { onChangeExercise() }) {
                    Text(stringResource(Res.string.workout_change))
                }
            }
            Spacer(Modifier.height(16.dp))
            //CONTENT
            ExerciseInfoComponent(
                exerciseInfo = exerciseInfo,
                changeQtyButtons = true,
                quantityChangeAction = { increase -> onChangeQuantity(increase) }
            )

            Spacer(Modifier.height(40.dp))
        }

        // 🔒 STICKY BUTTON
        if (exerciseInfo.quantityDirty) {
            Button(
                onClick = onQuantitySave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(stringResource(Res.string.workout_qty_save))
            }
        } else {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(stringResource(Res.string.workout_close))
            }
        }
    }
}



val EXE_FK = BuiltInExerciseRegistry.get(BuiltInExerciseId.FLUTTER_KICKS)


@Preview
@Composable
private fun ExerciseBottomSheetPrev() {
    AppTheme {
        val exerciseInfo = EXE_FK.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        WorkoutExerciseInfoBottomSheet(
            exerciseInfo = exerciseInfo.copy(
                quantityValue = 17,
                descriptionMarkdown = markdown
            ),
            onDismiss = {},
            onChangeExercise = {},
            onChangeQuantity = {},
            onQuantitySave = {},
        )
    }
}