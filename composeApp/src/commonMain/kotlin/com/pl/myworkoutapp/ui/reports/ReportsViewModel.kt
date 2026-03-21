package com.pl.myworkoutapp.ui.reports

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReportsViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        ReportsUiState()
    )
    val state: StateFlow<ReportsUiState> = _state

    fun onAction(action: ReportsAction) {
        println("Got action: $action")
    }
}