package com.pl.myworkoutapp.ui.app

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.*

class AppStateHolder {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state

    fun setWorkoutActive(active: Boolean) {
        _state.update { it.copy(isWorkoutActive = active) }
    }

    fun setThemeColor(themeColor: Color?) {
        _state.update { it.copy(themeColor = themeColor) }
    }

    fun setCameraActive(active: Boolean) {
        _state.update { it.copy(isCameraActive = active) }
    }

    fun setWorkoutExerciseInfoActive(active: Boolean) {
        _state.update { it.copy(isWorkoutExerciseInfoActive = active) }
    }

    fun setExerciseEditorActive(active: Boolean) {
        _state.update { it.copy(isExerciseEditorActive = active) }
    }

    fun setWorkoutEditorActive(active: Boolean) {
        _state.update { it.copy(isWorkoutEditorActive = active) }
    }

}