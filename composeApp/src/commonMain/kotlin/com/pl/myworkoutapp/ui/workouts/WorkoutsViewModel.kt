package com.pl.myworkoutapp.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.usecase.GetMainWorkoutsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val getMainWorkoutsUseCase: GetMainWorkoutsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutsUiState(isLoading = true))
    val state: StateFlow<WorkoutsUiState> = _state

    private val _events = Channel<WorkoutsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    init {
        observeWorkouts()
    }

    private fun observeWorkouts() {
        viewModelScope.launch {
            getMainWorkoutsUseCase.execute().collect { workouts ->
                _state.value = WorkoutsUiState(
                    isLoading = false,
                    workouts = workouts.map { it.toUi() }
                )
            }
        }
    }

    fun onAction(action: WorkoutsAction) {
        println("Got action: $action")
        when (action) {
            is WorkoutsAction.OnPageChanged -> Unit
            is WorkoutsAction.ShowWorkoutDetails -> showWorkoutDetails(action.workoutId)
            WorkoutsAction.AddExercise -> addExercise()
            WorkoutsAction.AddWorkout -> addWorkout()
        }
    }

    private fun showWorkoutDetails(workoutId: WorkoutId) {
        viewModelScope.launch {
            _events.send(WorkoutsEvent.NavToWorkoutDetails(workoutId))
        }
    }

    private fun addExercise() {
        viewModelScope.launch {
            _events.send(WorkoutsEvent.NavToExerciseEditor(ExerciseId.Custom.NEW))
        }
    }

    private fun addWorkout() {
        viewModelScope.launch {
            _events.send(WorkoutsEvent.NavToWorkoutDetails(WorkoutId.Custom.NEW))
        }
    }
}