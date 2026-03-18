package com.pl.myworkoutapp.ui.plans

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlansViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        PlansUiState()
    )
    val state: StateFlow<PlansUiState> = _state

    fun onAction(action: PlansAction) {
        println("Got action: $action")
    }
}