package com.pl.myworkoutapp.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        SettingsUiState()
    )
    val state: StateFlow<SettingsUiState> = _state

    fun onAction(action: SettingsAction) {
        println("Got action: $action")
    }

}