package com.pl.myworkoutapp

import androidx.compose.ui.window.ComposeUIViewController
import com.pl.myworkoutapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }