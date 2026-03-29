package com.pl.myworkoutapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pl.myworkoutapp.di.initKoin
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyWorkoutApplication",
        icon = painterResource(Res.drawable.app_icon)
    ) {
        App()
    }
}