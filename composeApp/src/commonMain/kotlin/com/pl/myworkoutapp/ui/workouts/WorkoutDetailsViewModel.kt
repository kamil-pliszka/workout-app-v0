package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem
import com.pl.myworkoutapp.domain.model.workout.toWorkoutIdOrNull
import com.pl.myworkoutapp.ui.common.loadExerciseDescription
import com.pl.myworkoutapp.ui.exercises.ExerciseInfoUiModel
import com.pl.myworkoutapp.ui.exercises.mapQuantityValue
import com.pl.myworkoutapp.ui.exercises.quantityChange
import com.pl.myworkoutapp.ui.exercises.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.compose_multiplatform

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
        loadWorkoutFromParam()
    }

    private fun loadWorkoutFromParam() {
        val workoutId = workoutIdParam.toWorkoutIdOrNull()
        workoutId?.let {
            loadWorkoutById(workoutId)
        }
    }

    private fun loadWorkoutById(workoutId: WorkoutId) {
        viewModelScope.launch {
            val workout = repository.getWorkout(workoutId)
            _state.value = WorkoutDetailsUiState(
                isLoading = false,
                workout = transform(workout) { exerciseId ->
                    repository.getExercise(exerciseId)
                },
                isDirty = false,
            )
            println("Wczytany trening dla: $workoutIdParam : $workoutId")
        }
    }


    fun onAction(action: WorkoutDetailsAction) {
        Log.d("WorkoutDetail","Got action: $action")
        when (action) {
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

            WorkoutDetailsAction.ExerciseExchange -> {
                _state.update { it.copy(showExercisePicker = true) }
            }

            is WorkoutDetailsAction.ChangeQuantity -> {
                changeQuantity(action.increase)
            }

            WorkoutDetailsAction.ExerciseSave -> {
                exerciseSave()
            }

            WorkoutDetailsAction.ExerciseNext -> {
                showNextExercise()
            }
            WorkoutDetailsAction.ExercisePrev -> {
                showPrevExercise()
            }
            WorkoutDetailsAction.ExerciseReset -> {
                resetExercise()
            }

            is WorkoutDetailsAction.ExercisePicked -> {
                println("EXE PICKED: ${action.exerciseId}")
                _state.update { it.copy(showExercisePicker = false) }
                action.exerciseId?.let {
                    currentExerciseExchange(action.exerciseId)
                }
            }

            WorkoutDetailsAction.OnStartWorkout -> {
                startWorkout()
            }

            WorkoutDetailsAction.OnSaveWorkout -> {
                saveWorkout()
            }

            WorkoutDetailsAction.OnResetWorkout -> {
                resetWorkout()
            }
        }
    }

    private suspend fun prepareExerciseInfo(exerciseId: ExerciseId): ExerciseInfoUiModel {
        val exercise: Exercise = repository.getExercise(exerciseId)
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
        return exerciseInfo.copy( descriptionMarkdown = exerciseMarkdown )
    }

    private fun showExerciseInfo(exe: ExerciseUiItem) {
        viewModelScope.launch {
            val exerciseInfo = prepareExerciseInfo(exe.exerciseId)
            val exes = _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            val current = exes.indexOf(exe) + 1
            val total = exes.size
            _state.update {
                it.copy(
                    selectedItem = exe,
                    exerciseInfo = exerciseInfo.copy(
                        quantityValue = exe.quantityValue,
                        current = current,
                        total = total,
                    )
                )
            }
        }
    }

    private fun showNextExercise() {
        _state.value.selectedItem?.let { item ->
            val exes = _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            val current = exes.indexOf(item)
            val next = exes.getOrNull(current + 1)
            next?.let {
                showExerciseInfo(next)
            }
        }
    }

    private fun showPrevExercise() {
        _state.value.selectedItem?.let { item ->
            val exes = _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            val current = exes.indexOf(item)
            val prev = exes.getOrNull(current - 1)
            prev?.let {
                showExerciseInfo(prev)
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

    private fun exerciseSave() {
        _state.value.exerciseInfo?.let { exerciseInfo ->
            _state.value.selectedItem?.let { item ->
                if (item is ExerciseUiItem && exerciseInfo.quantityValue != null) {
                    val newSelectedItem = item.copy(
                        exerciseId = exerciseInfo.exerciseId,
                        quantityValue = exerciseInfo.quantityValue,
                        quantityType = exerciseInfo.quantityType,
                        name = exerciseInfo.name,
                        icon = exerciseInfo.icon ?: Res.drawable.compose_multiplatform, //TODO - rozwiązać problem
                    )
                    _state.update {
                        it.copy(
                            workout = it.workout?.copy(
                                items = it.workout.items.map { i ->
                                    if (i == item) newSelectedItem else i
                                }
                            ),
                            isDirty = true,
                            //selectedItem = newSelectedItem,
                            //exerciseInfo = exerciseInfo.copy(quantityDirty = false)
                            selectedItem = null,//zamkniecie bottom sheet
                            exerciseInfo = null,//zamkniecie bottom sheet
                        )
                    }
                }
            }
        }
    }

    private fun resetExercise() {
        /*_state.value.exerciseInfo?.let { exerciseInfo ->
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
                        quantityValue = originalValue,
                        quantityDirty = false
                    )
                )
            }
            println("originalValue: $originalValue")
        }*/
        _state.value.selectedItem?.let {selectedItem ->
            if (selectedItem is ExerciseUiItem) {
                showExerciseInfo(selectedItem)
            }
        }
    }


    private fun currentExerciseExchange(exerciseId: ExerciseId) {
        viewModelScope.launch {
            _state.value.exerciseInfo?.let { currentExerciseInfo ->
                val newExerciseInfo = prepareExerciseInfo(exerciseId)
                //pozostaje zaktualizować qty
                val newQuantityValue = mapQuantityValue(
                    currentExerciseInfo.quantityType,
                    currentExerciseInfo.quantityValue ?: 1,
                    newExerciseInfo.quantityType
                )
                _state.update {
                    it.copy(
                        exerciseInfo = newExerciseInfo.copy(
                            quantityValue = newQuantityValue,
                            quantityDirty = true
                        )
                    )
                }
            }
        }
    }


    private fun startWorkout() {
        println("i co jeszcze?")
    }

    private fun resetWorkout() {
        //ważne, tutaj używamy id zapisanego treningu(może sięzmienić po edycji i zapisaniu wbudowanego treningu)
        val workoutId = _state.value.workout?.workout?.workoutId ?: workoutIdParam.toWorkoutIdOrNull()
        workoutId?.let {
            loadWorkoutById(workoutId)
        }
    }

    private fun saveWorkout() {
        viewModelScope.launch {
            println("saving")
            _state.value.workout?.let { workoutWithExercisesUi ->
                val workoutId = workoutWithExercisesUi.workout.workoutId
                val items : List<WorkoutItem> = toDomain(workoutWithExercisesUi.items)
                println("Saving workout: $workoutId")
                items.forEachIndexed { index, item ->
                    println("ITEM[$index]: $item")
                }
                val basedOn : WorkoutId.BuiltIn? = when(workoutId) {
                    is WorkoutId.BuiltIn -> workoutId
                    is WorkoutId.Custom -> workoutWithExercisesUi.workout.basedOn
                }
                val customId: WorkoutId.Custom = when(workoutId) {
                    is WorkoutId.BuiltIn -> WorkoutId.Custom.NEW
                    is WorkoutId.Custom -> workoutId
                }
                val customWorkout = CustomWorkout(
                    id = customId,
                    name = null,//TODO
                    description = null,//TODO
                    imageUri = null,//TODO
                    basedOn = basedOn,
                    difficulty = workoutWithExercisesUi.workout.difficulty,
                    items = items
                )
                val savedWorkoutId = repository.saveCustomWorkout(customWorkout)
                println("Saved as : $savedWorkoutId")
                loadWorkoutById(savedWorkoutId)
            }
        }
    }


}