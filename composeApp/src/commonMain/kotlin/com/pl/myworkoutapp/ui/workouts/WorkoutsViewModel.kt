package com.pl.myworkoutapp.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.domain.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val repository: WorkoutRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(
        WorkoutsUiState(isLoading = false)
    )
    val state: StateFlow<WorkoutsUiState> = _state

    fun onAction(action: WorkoutsAction) {
        println("Got action: $action")
    }

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            val workouts = repository.getWorkouts()
            _state.value = WorkoutsUiState(
                isLoading = false,
                workouts = workouts.map { workout ->
                    workout.toUi()
                }
            )
            println("Wczytane treningi: ${workouts.size}")
        }
    }




}