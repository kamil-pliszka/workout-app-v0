package com.pl.myworkoutapp.ui.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView

@Composable
actual fun KeepScreenOnEffect(
    controller: KeepScreenController
) {
    val enabled by controller.enabled.collectAsState()
    val view = LocalView.current

    DisposableEffect(enabled) {
        val activity = view.context as? Activity
        val window = activity?.window
        println("KeepScreenOnEffect.Android: $enabled")
        println("KeepScreenOnEffect.Android: activity = ${activity != null}, window = ${window != null}")

        if (enabled) {
            println("KeepScreenOnEffect.Android: enable")
            window?.addFlags(
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        onDispose {
            println("KeepScreenOnEffect.Android: dispose")
            window?.clearFlags(
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }
}