package com.pl.myworkoutapp

import androidx.compose.ui.graphics.Color

data class AppState(
    val isWorkoutActive: Boolean = false,
    val isCameraActive: Boolean = false,
    val isHiddenNavigation: Boolean = false,//TODO - moze nazwać jakoś lepiej
    val themeColor: Color? = null,
) {
    val showNavigationBar: Boolean
        get() = !isHiddenNavigation && !isWorkoutActive && !isCameraActive
}