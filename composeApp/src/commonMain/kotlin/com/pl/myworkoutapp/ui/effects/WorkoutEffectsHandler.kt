//package com.pl.myworkoutapp.ui.effects
//
//import androidx.compose.runtime.*
//import kotlinx.coroutines.flow.Flow
//
//@Composable
//fun WorkoutEffectsHandler(
//    effects: Flow<WorkoutEffect>
//) {
//    val platform = LocalPlatformEffects.current
//
//    LaunchedEffect(Unit) {
//        effects.collect { effect ->
//            when (effect) {
//                is WorkoutEffect.KeepScreenOn -> {
//                    platform.keepScreenOn(true)
//                }
//                is WorkoutEffect.AllowScreenOff -> {
//                    platform.keepScreenOn(false)
//                }
//                is WorkoutEffect.Vibrate -> {
//                    platform.vibrate(effect.durationMs)
//                }
//                is WorkoutEffect.PlaySound -> {
//                    platform.playSound(effect.type)
//                }
//                is WorkoutEffect.Speak -> {
//                    platform.speak(effect.text)
//                }
//            }
//        }
//    }
//}