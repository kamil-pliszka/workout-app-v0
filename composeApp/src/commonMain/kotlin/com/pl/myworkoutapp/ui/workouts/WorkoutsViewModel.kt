package com.pl.myworkoutapp.ui.workouts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkoutsViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        WorkoutsUiState()
    )
    val state: StateFlow<WorkoutsUiState> = _state

    fun onAction(action: WorkoutsAction) {
        println("Got action: $action")
    }

}