//package com.pl.myworkoutapp.ui.effects
//
//sealed interface WorkoutEffect {
//
//    // screen
//    data object KeepScreenOn : WorkoutEffect
//    data object AllowScreenOff : WorkoutEffect
//
//    // vibration
//    data class Vibrate(val durationMs: Long) : WorkoutEffect
//
//    // sound
//    data class PlaySound(val type: SoundType) : WorkoutEffect
//
//    // TTS
//    data class Speak(val text: String) : WorkoutEffect
//}
//
//enum class SoundType {
//    START,
//    NEXT,
//    REST,
//    FINISH
//}