package com.pl.myworkoutapp.ui.execution.engine

import com.pl.myworkoutapp.ui.effects.PlatformEffects
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.speech_text_half_time
import myworkoutapplication.composeapp.generated.resources.speech_text_rest_finished
import org.jetbrains.compose.resources.getString

class ExecutionEffectHandler(
    private val effects: PlatformEffects,
) {
    suspend fun handle(effect: ExecutionEffect) {
        when (effect) {
            is ExecutionEffect.Vibrate -> {
                effects.vibrate(effect.durationMs)
            }
            is ExecutionEffect.PlaySound -> {
                effects.playSound(effect.type)
            }
            is ExecutionEffect.KeepScreenOn -> {
                println("ExecutionEffectHandler: ${effect.enabled}")
                effects.keepScreenOn(effect.enabled)
            }
            is ExecutionEffect.Speak -> {
                effects.speak(effect.text.resolveText())
            }
        }
    }

    private suspend fun SpeechText.resolveText(): String {
        return when (this) {
            is SpeechText.Countdown -> this.seconds.toString()
            SpeechText.HalfTime -> getString(Res.string.speech_text_half_time)
            SpeechText.RestFinished -> getString(Res.string.speech_text_rest_finished)
        }
    }
}