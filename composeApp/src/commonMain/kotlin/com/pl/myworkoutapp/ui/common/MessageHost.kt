package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource


@Composable
fun MessageHost(
    message: UiMessage?,
    onClose: () -> Unit
) {
    println("MessageHost: $message")
    if (message == null) return

    val (icon, containerColor) = when (message) {
        is UiMessage.Error -> Res.drawable.ic_error to MaterialTheme.colorScheme.errorContainer
        is UiMessage.Info -> Res.drawable.ic_info to MaterialTheme.colorScheme.secondaryContainer
        is UiMessage.Success -> Res.drawable.ic_success to MaterialTheme.colorScheme.primaryContainer
    }
    val color = when (message) {
        is UiMessage.Error -> MaterialTheme.colorScheme.error
        is UiMessage.Info -> MaterialTheme.colorScheme.secondary
        is UiMessage.Success -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 24.dp)
        ,
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = containerColor,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp
        ) {
            Box {
                // 🔹 CONTENT
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .padding(end = 32.dp), // 👈 miejsce na X
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = color,
                    )
                    Spacer(Modifier.width(20.dp))
                    SelectionContainer {
                        Text(
                            text = message.text.asString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

               // 🔹 CLOSE BUTTON
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "Close",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MessagePreviewError() {
    AppTheme {
        MessageHost(
            UiMessage.Error(
                text = "Wystąpił niespodziewany wyjątek, jedyna szansa w nadzieji".asUiText()
            ),
            onClose = {}
        )
    }
}


@Preview
@Composable
private fun MessagePreviewInfo() {
    AppTheme {
        MessageHost(
            UiMessage.Info(
                text = "Informacja z systemu?\nSystem z\ninformacji.".asUiText()
            ),
            onClose = {}
        )
    }
}

@Preview
@Composable
private fun MessagePreviewSuccess() {
    AppTheme {
        MessageHost(
            UiMessage.Success(
                text = "Operacja zakończyła się zanim się zaczęła.".asUiText()
            ),
            onClose = {}
        )
    }
}