package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pl.myworkoutapp.ui.theme.AppTheme

@Composable
fun CustomDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Czy jesteś pewien, że chcesz usunąć to zdjęcie?",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(onClick = onDismiss) {
                        Text("ANULUJ")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error//Color(0xFFFF5722)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("USUŃ", color = MaterialTheme.colorScheme.onError)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun CustomDeleteDialogPreview() {
    AppTheme {
        CustomDeleteDialog(
            onDismiss = { },
            onConfirm = { },
        )
    }
}