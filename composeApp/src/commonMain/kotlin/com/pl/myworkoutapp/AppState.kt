package com.pl.myworkoutapp

import androidx.compose.ui.graphics.Color

data class AppState(
    val isWorkoutActive: Boolean = false,
    val isCameraActive: Boolean = false,
    val isWorkoutExerciseInfoActive: Boolean = false,
    val isExerciseEditorActive: Boolean = false,
    val isWorkoutEditorActive: Boolean = false,
    val themeColor: Color? = null,
) {
    val showNavigationBar: Boolean
        get() = !isWorkoutActive && !isCameraActive && !isWorkoutExerciseInfoActive
                && !isExerciseEditorActive && !isWorkoutEditorActive
}