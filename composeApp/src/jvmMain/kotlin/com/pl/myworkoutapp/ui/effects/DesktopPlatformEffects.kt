package com.pl.myworkoutapp.ui.effects

import java.awt.Toolkit

class DesktopPlatformEffects : PlatformEffects {

    override fun keepScreenOn(enabled: Boolean) {
        // brak sensownego API → no-op
    }

    override fun vibrate(durationMs: Long) {
        // desktop → fallback: beep
        Toolkit.getDefaultToolkit().beep()
    }

    override fun playSound(type: SoundType) {
        // najprostszy fallback
        Toolkit.getDefaultToolkit().beep()
    }

    override fun speak(text: String) {
        // brak natywnego TTS bez zależności
        println("TTS: $text")
    }
}