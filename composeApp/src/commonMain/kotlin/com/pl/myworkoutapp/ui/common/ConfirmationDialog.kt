package com.pl.myworkoutapp.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.confirmation_are_you_sure
import myworkoutapplication.composeapp.generated.resources.confirmation_cancel_text
import myworkoutapplication.composeapp.generated.resources.confirmation_confitm_text
import myworkoutapplication.composeapp.generated.resources.confirmation_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmationDialog(
    title: String = stringResource(Res.string.confirmation_title),
    text: String = stringResource(Res.string.confirmation_are_you_sure),
    onConfirm: () -> Unit,
    //confirmContent: @Composable RowScope.() -> Unit = { Text("OK") },
    confirmText : String = stringResource(Res.string.confirmation_confitm_text),
    confirmColor: Color = Color.Unspecified,
    onCancel: () -> Unit,
    cancelText : String = stringResource(Res.string.confirmation_cancel_text),
    cancelColor: Color = Color.Unspecified,
) {

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },

        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(confirmText, color = confirmColor)
            }
        },

        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(cancelText, color = cancelColor)
            }
        }
    )
}