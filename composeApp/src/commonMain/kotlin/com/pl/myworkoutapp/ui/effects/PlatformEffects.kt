package com.pl.myworkoutapp.ui.effects

interface PlatformEffects {
    fun keepScreenOn(enabled: Boolean)
    fun vibrate(durationMs: Long)
    fun playSound(type: SoundType)
    fun speak(text: String)
}
