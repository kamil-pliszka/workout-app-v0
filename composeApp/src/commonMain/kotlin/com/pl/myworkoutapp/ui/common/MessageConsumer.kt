package com.pl.myworkoutapp.ui.common

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

@Composable
fun MessageConsumer(
    modifier: Modifier = Modifier,
    messages: Flow<UiMessage>,
) {
    var currentMessage by remember { mutableStateOf<UiMessage?>(null) }
    val scope = rememberCoroutineScope()
    var dismissJob by remember { mutableStateOf<Job?>(null) }

    // 🔥 collect message events
    ObserveAsEvents(messages) { message ->
        dismissJob?.cancel()
        currentMessage = message
        // auto-dismiss po czasie
        if (message !is UiMessage.Error) {//error trzeba zamknąć samodzielnie krzyżykiem
            dismissJob = scope.launch {
                delay(
                    when (message) {
                        is UiMessage.Success -> 5000
                        is UiMessage.Info -> 3500
                    }
                )
                currentMessage = null
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(
            visible = currentMessage != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter) // 🔥 KONIECZNE
        ) {
            MessageHost(
                message = currentMessage,
                onClose = {
                    currentMessage = null
                }
            )
        }
    }
}