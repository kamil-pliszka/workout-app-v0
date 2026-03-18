package com.pl.myworkoutapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pl.myworkoutapp.di.initKoin
import com.pl.myworkoutapp.ui.effects.DesktopPlatformEffects

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyWorkoutApplication",
    ) {
        App()
    }
}