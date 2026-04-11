package com.pl.myworkoutapp

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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

    fun setHideNavigation(hidden: Boolean) {
        _state.update { it.copy(isHiddenNavigation = hidden) }
    }

}