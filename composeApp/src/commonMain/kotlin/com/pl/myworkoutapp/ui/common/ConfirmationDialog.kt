package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmationDialog(
    title: String = stringResource(Res.string.confirmation_title),
    text: String = stringResource(Res.string.confirmation_are_you_sure),
    onConfirm: () -> Unit,
    //confirmContent: @Composable RowScope.() -> Unit = { Text("OK") },
    confirmText: String = stringResource(Res.string.confirmation_confitm_text),
    confirmButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    confirmTextColor: Color = Color.Unspecified,
    onCancel: () -> Unit,
    cancelText: String = stringResource(Res.string.confirmation_cancel_text),
    cancelButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    cancelTextColor: Color = Color.Unspecified,
) {

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
                ) {
                    Button(
                        onClick = onCancel,
                        colors = cancelButtonColors,
                    ) {
                        Text(cancelText, color = cancelTextColor)
                    }

                    Button(
                        onClick = onConfirm,
                        colors = confirmButtonColors,
                    ) {
                        Text(confirmText, color = confirmTextColor)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmPreview() {
    AppTheme {
        ConfirmationDialog(
            title = stringResource(Res.string.workout_editor_delete_title),
            text = stringResource(Res.string.workout_editor_delete_question),
            confirmText = stringResource(Res.string.btn_delete),
            confirmButtonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
            //confirmTextColor = MaterialTheme.colorScheme.onError,
            onConfirm = { },
            onCancel = { },
        )
    }
}