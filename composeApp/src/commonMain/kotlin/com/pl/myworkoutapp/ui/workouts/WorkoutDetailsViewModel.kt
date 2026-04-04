package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.workout.toWorkoutIdOrNull
import com.pl.myworkoutapp.ui.common.loadExerciseDescription
import com.pl.myworkoutapp.ui.exercises.quantityChange
import com.pl.myworkoutapp.ui.exercises.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel(
    private val repository: WorkoutRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val workoutIdParam: String =
        savedStateHandle["workoutId"] ?: error("workoutId is required")
    private val _state = MutableStateFlow(
        WorkoutDetailsUiState(isLoading = true)
    )
    val state: StateFlow<WorkoutDetailsUiState> = _state

    init {
        loadWorkout()
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            val workoutId = workoutIdParam.toWorkoutIdOrNull()
            val workout = workoutId?.let { repository.getWorkout(workoutId) }
            _state.value = WorkoutDetailsUiState(
                isLoading = false,
                workout = workout?.let { transform(it) }
            )
            println("Wczytane trening dla: $workoutIdParam : $workoutId")
        }
    }


    fun onAction(action: WorkoutDetailsAction) {
        println("Got action: $action")
        when (action) {
            WorkoutDetailsAction.OnStartWorkout -> TODO()

            is WorkoutDetailsAction.ShowExerciseInfo -> {
                when (action.exercise) {
                    is CircuitUiItem -> {} //nic nie robimy z Circuit
                    is ExerciseUiItem -> {
                        showExerciseInfo(action.exercise)
                    }
                }
            }

            WorkoutDetailsAction.CloseExerciseInfo -> {
                _state.update { it.copy(selectedItem = null, exerciseInfo = null) }
            }

            WorkoutDetailsAction.ChangeExercise -> {
                //TODO
            }

            is WorkoutDetailsAction.ChangeQuantity -> {
                changeQuantity(action.increase)
            }

            WorkoutDetailsAction.QuantitySave -> {
                quantitySave()
            }
        }
    }

    private fun showExerciseInfo(exe: ExerciseUiItem) {
        viewModelScope.launch {
            val exercise: Exercise = repository.getExercise(exe.exerciseId)
            val exerciseInfo = exercise.toUi()
            val exerciseMarkdown = exerciseInfo
                .takeIf { it.customDesc == null }
                ?.descExerciseId
                ?.let { id ->
                    loadExerciseDescription(
                        exerciseId = id,
                        lang = Locale.current.language
                    )
                }
            _state.update {
                it.copy(
                    selectedItem = exe,
                    exerciseInfo = exerciseInfo.copy(
                        quantityValue = exe.quantityValue,
                        descriptionMarkdown = exerciseMarkdown
                    )
                )
            }
        }
    }

    private fun changeQuantity(increase: Boolean) {
        _state.value.exerciseInfo?.let { exerciseInfo ->
            val newQuantityValue = quantityChange(
                type = exerciseInfo.quantityType,
                currentQuantityValua = exerciseInfo.quantityValue ?: 0,
                increase = increase
            )
            val originalValue = _state.value.selectedItem?.let { item ->
                if (item is ExerciseUiItem) {
                    item.quantityValue
                } else {
                    null
                }
            }

            _state.update {
                it.copy(
                    exerciseInfo = exerciseInfo.copy(
                        quantityValue = newQuantityValue,
                        quantityDirty = originalValue != newQuantityValue
                    )
                )
            }
            println("state exerciseInfo.quantityDirty: ${_state.value.exerciseInfo?.quantityDirty}")
            println("originalValue: $originalValue")
            println("newQuantityValue: $newQuantityValue")
            //println("state selectedItem: ${_state.value.selectedItem}")
        }
    }

    private fun quantitySave() {
        _state.value.exerciseInfo?.let { exerciseInfo ->
            _state.value.selectedItem?.let { item ->
                if (item is ExerciseUiItem && exerciseInfo.quantityValue != null) {
                    _state.update {
                        it.copy(
                            selectedItem = item.copy(
                                quantityValue = exerciseInfo.quantityValue
                            ),
                            exerciseInfo = exerciseInfo.copy(quantityDirty = false)
                        )
                    }
                }
            }
        }
    }


}