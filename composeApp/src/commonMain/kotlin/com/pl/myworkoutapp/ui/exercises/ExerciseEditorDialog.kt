package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExerciseEditorDialog(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    AlertDialog(
        onDismissRequest = { onAction(ExerciseEditorAction.OnDismissRequest)},
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Cyan)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(onClick = { onAction(ExerciseEditorAction.OnDismissRequest)}) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.btn_cancel)
                    )
                }
                TextButton(onClick = { onAction(ExerciseEditorAction.OnDeleteAction) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete_forever),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = stringResource(Res.string.btn_delete)
                    )
                }
                TextButton(onClick = { onAction(ExerciseEditorAction.OnSaveAction) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.btn_save)
                    )
                }
            }
        },
        //dismissButton = {},
        title = {
            Column {
                //Text(stringResource(Res.string.edit_measure_title, state.date.toDateString()))
                Text("create/edit: $state")
                HorizontalDivider()
            }
        },
        text = {
            ExerciseEditorContent(
                modifier = Modifier,
                state = state,
                onAction = onAction
            )
        }
    )
}

@Composable
fun ExerciseEditorContent(
    modifier: Modifier = Modifier,
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    Text("Exe contetn")

}






////////////////////////////////////////////////////////////////////////////////////////////////////

@Preview
@Composable
private fun ExerciseEditorPreview() {
    AppTheme {
        ExerciseEditorDialog(
            state = ExerciseEditorUiState(
            ),
            onAction = { }
        )
    }
}

