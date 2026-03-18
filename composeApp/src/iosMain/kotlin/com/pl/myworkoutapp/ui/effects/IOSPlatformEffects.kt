package com.pl.myworkoutapp.ui.effects

import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance
import platform.UIKit.UIApplication
import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.AudioToolbox.kSystemSoundID_Vibrate

/**
 * uwagi
 * idleTimerDisabled → działa globalnie (brak per-screen)
 *
 * wibracja: brak kontroli długości tylko trigger
 *
 * TTS (AVSpeechSynthesizer) działa bardzo dobrze out-of-the-box
 */
class IOSPlatformEffects : PlatformEffects {

    private val synthesizer = AVSpeechSynthesizer()

    override fun keepScreenOn(enabled: Boolean) {
        UIApplication.sharedApplication.idleTimerDisabled = enabled
    }

    override fun vibrate(durationMs: Long) {
        // iOS ignoruje duration – tylko pojedyncza wibracja
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
    }

    override fun playSound(type: SoundType) {
        // minimalna implementacja (system sound można dodać później)
        // TODO: AVAudioPlayer / SystemSoundID
    }

    override fun speak(text: String) {
        val utterance = AVSpeechUtterance.speechUtteranceWithString(text)
        synthesizer.speakUtterance(utterance)
    }
}