package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.ui.effects.SoundType

sealed interface ExecutionEffect {
    data class Vibrate(
        val durationMs: Long = 200
    ) : ExecutionEffect
    data class PlaySound(
        val type: SoundType
    ) : ExecutionEffect
    data class KeepScreenOn(
        val enabled: Boolean
    ) : ExecutionEffect
    data class Speak(
        val text: SpeechText
    ) : ExecutionEffect
}

sealed interface SpeechText {
    data object HalfTime : SpeechText
    data object RestFinished : SpeechText
    data class Countdown(
        val seconds: Int
    ) : SpeechText
}