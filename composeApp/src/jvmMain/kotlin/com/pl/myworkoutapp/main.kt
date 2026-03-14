package com.pl.myworkoutapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pl.myworkoutapp.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyWorkoutApplication",
    ) {
        App()
    }
}