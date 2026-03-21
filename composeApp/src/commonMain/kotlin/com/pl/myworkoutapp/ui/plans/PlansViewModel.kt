package com.pl.myworkoutapp.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.domain.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlansViewModel(
    private val repository: WorkoutRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PlansUiState(isLoading = true))
    val state: StateFlow<PlansUiState> = _state

    init {
        loadPlans()
    }

    private fun loadPlans() {
        viewModelScope.launch {
            val plans = repository.getPlans()

            _state.value = PlansUiState(
                isLoading = false,
                plans = plans.map { it.toUi() }
            )

            println("Wczytane plany: ${plans.size}")
        }
    }


    fun onAction(action: PlansAction) {
        println("Got action: $action")
    }
}