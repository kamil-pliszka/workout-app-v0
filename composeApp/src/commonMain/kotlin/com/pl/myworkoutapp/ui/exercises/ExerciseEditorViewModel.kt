package com.pl.myworkoutapp.ui.exercises

import androidx.lifecycle.*
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.toExerciseIdOrNull
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ExerciseEditorViewModel(
    private val repository: WorkoutRepository,
    exerciseCoordinator: ExerciseEditorCoordinator,
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator
) : ViewModel() {
    private val exerciseIdParam: String =
        savedStateHandle["exerciseId"] ?: error("exerciseId is required")
    private val _state = MutableStateFlow(
        ExerciseEditorUiState(isLoading = true)
    )
    val state: StateFlow<ExerciseEditorUiState> = _state

    init {
        loadExerciseFromParam()
    }

    private fun loadExerciseFromParam() {
        val exerciseId = exerciseIdParam.toExerciseIdOrNull()
        exerciseId?.let {
            if (exerciseId == ExerciseId.Custom.NEW) {
                prepareNewExercise()
            } else {
                loadExerciseById(exerciseId)
            }
        }
    }

    private fun prepareNewExercise() {
        viewModelScope.launch {
            println("prepare empty exercise")
            _state.value = ExerciseEditorUiState(
                isLoading = false,
                isDirty = false,
//                workout = transform(workout) { exerciseId ->
//                    repository.getExercise(exerciseId)
//                },
            )
        }
    }

    private fun loadExerciseById(exerciseId: ExerciseId) {
        viewModelScope.launch {
            val exercise = repository.getExercise(exerciseId)
            println("Got exercise: $exercise")
            _state.value = ExerciseEditorUiState(
                isLoading = false,
                isDirty = false,
//                workout = transform(workout) { exerciseId ->
//                    repository.getExercise(exerciseId)
//                },
            )
        }
    }


    fun onAction(action: ExerciseEditorAction) {
        println("Got action: $action")
        when(action) {
            ExerciseEditorAction.OnDeleteAction -> TODO()
            ExerciseEditorAction.OnDismissRequest -> {
                //tutaj trzeba sprawdzić czy wprowadzono jakieś zmiany, i jeśli tak, to dodać komunikat/ostrzeżenie
                appNavigator.closeDialog()
            }
            ExerciseEditorAction.OnSaveAction -> TODO()
        }
    }


    override fun onCleared() {
        super.onCleared()
        println("VM onCleared: $this")
    }
}