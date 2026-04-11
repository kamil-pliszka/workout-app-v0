package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.btn_close
import org.jetbrains.compose.resources.stringResource


@Composable
fun ExercisePickerExeInfo(
    exerciseInfo: ExerciseInfoUiModel,
    onClose: () -> Unit
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
                .fillMaxHeight(0.89f)
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

                TextButton(onClick = onClose) {
                    Text(stringResource(Res.string.btn_close))
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
                    changeQtyButtons = false,
                    quantityChangeAction = {}
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
                Button(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.btn_close))
                }
            }
        }
    }
}


val EXE = BuiltInExerciseRegistry.get(BuiltInExerciseId.DEAD_BUG)

@Preview
@Composable
private fun ExercisePickerExeInfoPreview() {
    AppTheme {
        val exerciseInfo = EXE.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        ExercisePickerExeInfo(
            exerciseInfo = exerciseInfo.copy(
                //quantityValue = 11,
                descriptionMarkdown = markdown + ""
            ),
            onClose = {},
        )
    }

}

