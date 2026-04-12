package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.core.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface UiMessage {
    val text: UiText
    data class Error(override val text: UiText) : UiMessage
    data class Success(override val text: UiText) : UiMessage
    data class Info(override val text: UiText) : UiMessage
}

class MessageCoordinator {
    private val _messages = MutableSharedFlow<UiMessage>(
        extraBufferCapacity = 10,
        replay = 1
    )
    val messages = _messages.asSharedFlow()

    fun send(message: UiMessage) {
//        _messages.tryEmit(message)
//        check(_messages.tryEmit(message)) {
//            "Failed to emit message event: $_messages"
//        }
        if (!_messages.tryEmit(message)) {
            Log.e("MessageCoordinator", "Dropped message: $message")
        }
    }

    fun error(text: UiText) {
        send(UiMessage.Error(text))
    }

    fun success(text: UiText) {
        send(UiMessage.Success(text))
    }

    fun info(text: UiText) {
        send(UiMessage.Info(text))
    }
}