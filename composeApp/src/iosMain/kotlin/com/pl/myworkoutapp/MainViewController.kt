package com.pl.myworkoutapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.pl.myworkoutapp.di.initKoin
import com.pl.myworkoutapp.ui.effects.IOSPlatformEffects

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}