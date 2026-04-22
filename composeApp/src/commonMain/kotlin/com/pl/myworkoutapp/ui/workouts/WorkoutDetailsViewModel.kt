package com.pl.myworkoutapp.ui.workouts

import androidx.lifecycle.*
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.core.exceptionToString
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.domain.usecase.GetWorkoutWithExercisesUseCase
import com.pl.myworkoutapp.domain.usecase.SaveWorkoutUseCase
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.exercises.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.*

class WorkoutDetailsViewModel(
    private val repository: WorkoutRepository,
    savedStateHandle: SavedStateHandle,
    //private val messageCoordinator : MessageCoordinator,
    private val appStateHolder: AppStateHolder,
    private val editDelegate: WorkoutEditorDelegate,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val getWorkoutWithExercisesUseCase: GetWorkoutWithExercisesUseCase
) : ViewModel() {
    private val workoutIdParam: String =
        savedStateHandle["workoutId"] ?: error("workoutId is required")
    private val _state = MutableStateFlow(
        WorkoutDetailsUiState(isLoading = true)
    )
    val state: StateFlow<WorkoutDetailsUiState> = _state

    private val _events = Channel<WorkoutDetailsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun sendEvent(event: WorkoutDetailsEvent) {
        _events.send(event)
    }

    private suspend fun showExceptionAsMessage(e: Throwable) {
        sendEvent(
            WorkoutDetailsEvent.ShowError(
                Res.string.error_during_processing.asUiText(
                    exceptionToString(e)
                )
            )
        )
    }


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
            _state.update { it.copy(isLoading = true) }
            try {
                val result = getWorkoutWithExercisesUseCase.execute(workoutId)
                val workout = result.workout
                val exercises = result.exercises
                // Use the pre-fetched map in the transformer
                val uiWorkout = transform(workout, exercises)
                _state.update {
                    it.copy(
                        isLoading = false,
                        workout = uiWorkout,
                        isDirty = false
                    )
                }
            } catch (e: Throwable) {
                Log.e("WorkoutDetail", "Failed to load workout", e)
                _state.update { it.copy(isLoading = false) }
                showExceptionAsMessage(e)
            }
        }
    }

    fun onAction(action: WorkoutDetailsAction) {
        Log.d("WorkoutDetail", "Got action: $action")
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
                appStateHolder.setWorkoutExerciseInfoActive(false)
                _state.update { it.copy(selectedItem = null, exerciseInfo = null) }
            }

            WorkoutDetailsAction.ShowExercisePicker -> {
                _state.update { it.copy(showExercisePicker = true) }
            }

            is WorkoutDetailsAction.ChangeQuantity -> changeQuantity(action.increase)

            WorkoutDetailsAction.ExerciseSave -> exerciseSave()

            WorkoutDetailsAction.ExerciseNext -> showNextExercise()

            WorkoutDetailsAction.ExercisePrev -> showPrevExercise()

            WorkoutDetailsAction.ExerciseReset -> resetExercise()

            is WorkoutDetailsAction.ExercisePicked -> {
                println("EXE PICKED: ${action.exerciseId}")
                _state.update { it.copy(showExercisePicker = false) }
                if (_state.value.editableWorkout != null) {
                    exercisePickedForWorkoutEditor(action.exerciseId)
                } else {
                    action.exerciseId?.let {
                        currentExerciseExchange(action.exerciseId)
                    }
                }
            }

            WorkoutDetailsAction.OnStartWorkout -> startWorkout()

            WorkoutDetailsAction.OnSaveWorkout -> saveWorkout()

            WorkoutDetailsAction.OnResetWorkout -> resetWorkout()

            WorkoutDetailsAction.OnBack -> {
                viewModelScope.launch {
                    sendEvent(WorkoutDetailsEvent.Close)
                }
            }

            WorkoutDetailsAction.OnOpenEditor -> startWorkoutEditor()
            WorkoutDetailsAction.OnCloseEditor -> closeWorkoutEditor()
            WorkoutDetailsAction.OnSaveEditor -> saveWorkoutEditor()
            WorkoutDetailsAction.OnResetEditor -> resetWorkoutEditor()
            WorkoutDetailsAction.OnDeleteRequest -> TODO()
            WorkoutDetailsAction.OnTuneRequest -> TODO()

        }
    }

    fun onEditorAction(editAction: WorkoutEditorAction) {
        val current = _state.value.editableWorkout ?: return
        _state.update {
            it.copy(
                editableWorkout = editDelegate.reduce(current, editAction)
            )
        }
    }

    //TODO - a może upakowaćw akcję WorkoutEditorAction ?
    fun onCircuitEditorAction(circuitEditorAction: CircuitEditorAction) {
        val current = _state.value.editableWorkout?: return
        _state.update {
            it.copy(
                editableWorkout = editDelegate.reduce(current, circuitEditorAction)
            )
        }
    }

    private suspend fun prepareExerciseInfo(exerciseId: ExerciseId): ExerciseInfoUiModel {
        //TODO - coś z tym zrobić
        val exercise: Exercise = repository.getExercise(exerciseId)
        val exerciseInfo = exercise.toUi()
        val exerciseMarkdown = exerciseInfo.loadExerciseDescription()
        return exerciseInfo.copy(descriptionMarkdown = exerciseMarkdown)
    }

    private var exerciseInfoJob: Job? = null
    private fun showExerciseInfo(exe: ExerciseUiItem) {
        exerciseInfoJob?.cancel() // Cancel previous loading if user clicked fast
        exerciseInfoJob = viewModelScope.launch {
            val exerciseInfo = prepareExerciseInfo(exe.exerciseId)
            val exes =
                _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            //val current = exes.indexOf(exe) + 1
            val current = exes.indexOfFirst { it.key == exe.key } + 1
            appStateHolder.setWorkoutExerciseInfoActive(true)
            _state.update {
                it.copy(
                    selectedItem = exe,
                    exerciseInfo = exerciseInfo.copy(
                        quantityValue = exe.quantityValue,
                        current = current,
                        total = exes.size,
                    )
                )
            }
        }
    }

    private fun showNextExercise() {
        _state.value.selectedItem?.let { item ->
            val exes =
                _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            val current = exes.indexOfFirst { it.key == item.key }
            val next = exes.getOrNull(current + 1)
            next?.let {
                showExerciseInfo(next)
            }
        }
    }

    private fun showPrevExercise() {
        _state.value.selectedItem?.let { item ->
            val exes =
                _state.value.workout?.items?.filterIsInstance<ExerciseUiItem>() ?: emptyList()
            val current = exes.indexOfFirst { it.key == item.key }
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
        _state.update { currentState ->
            val exerciseInfo = currentState.exerciseInfo ?: return@update currentState
            val item = currentState.selectedItem as? ExerciseUiItem ?: return@update currentState
            val quantityValue = exerciseInfo.quantityValue ?: return@update currentState

            val newSelectedItem = item.copy(
                exerciseId = exerciseInfo.exerciseId,
                quantityValue = quantityValue,
                quantityType = exerciseInfo.quantityType,
                name = exerciseInfo.name,
                icon = exerciseInfo.icon
                    ?: Res.drawable.compose_multiplatform, //TODO - rozwiązać problem
            )
            currentState.copy(
                workout = currentState.workout?.copy(
                    items = currentState.workout.items.map { i ->
                        if (i.key == item.key) newSelectedItem else i
                    }
                ),
                isDirty = true,
                selectedItem = null,//zamkniecie bottom sheet
                exerciseInfo = null,//zamkniecie bottom sheet
            )
        }
    }

    private fun resetExercise() {
        _state.value.selectedItem?.let { selectedItem ->
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
        if (_state.value.isDirty) {
            viewModelScope.launch {
                sendEvent(WorkoutDetailsEvent.ShowError("state is dirty".asUiText()))
            }
            return
        }
        viewModelScope.launch {
            _state.value.workout?.let { w ->
                sendEvent(WorkoutDetailsEvent.NavToWorkoutExecution(w.workout.workoutId))
            }
        }
    }

    private fun resetWorkout() {
        //ważne, tutaj używamy id zapisanego treningu(może sięzmienić po edycji i zapisaniu wbudowanego treningu)
        val workoutId =
            _state.value.workout?.workout?.workoutId ?: workoutIdParam.toWorkoutIdOrNull()
        workoutId?.let {
            loadWorkoutById(workoutId)
        }
    }

    private fun saveWorkout() {//po zmianie danych ćwiczenia lub innych zmianach
        viewModelScope.launch {
            println("saving")
            _state.value.workout?.let { workoutWithExercisesUi ->
                val workoutId = workoutWithExercisesUi.workout.workoutId
                val items: List<WorkoutItem> = toDomain(workoutWithExercisesUi.items)

                val savedWorkoutId = saveWorkoutUseCase.execute(
                    workoutId = workoutId,
                    basedOn = workoutWithExercisesUi.workout.basedOn,
                    difficulty = workoutWithExercisesUi.workout.difficulty,
                    items = items
                )
                println("Saved as : $savedWorkoutId")

                //messageCoordinator.success(Res.string.workout_saved_success.asUiText())
                sendEvent(WorkoutDetailsEvent.ShowSuccess(Res.string.workout_saved_success.asUiText()))
                loadWorkoutById(savedWorkoutId)//TODO - zastanowić się
            }
        }
    }

    /*
    private fun updateEditable(
        block: (WorkoutWithExercisesUiModel) -> WorkoutWithExercisesUiModel
    ) {
        _state.update { state ->
            state.copy(editableWorkout = state.editableWorkout?.let(block))
        }
    }
    */


    private fun startWorkoutEditor() {
        //dodać ewentualnie jakieś sprawdzenia
        appStateHolder.setWorkoutEditorActive(true)
        _state.update {
            it.copy(editableWorkout = it.workout?.copy())
        }
    }

    private fun closeWorkoutEditor() {
        appStateHolder.setWorkoutEditorActive(false)
        _state.update {
            it.copy(editableWorkout = null)
        }
    }

    private fun saveWorkoutEditor() {
        appStateHolder.setWorkoutEditorActive(false)
        val currentEditable = _state.value.editableWorkout ?: return
        val originalWorkout = _state.value.workout ?: return
        println(".workout : " + (originalWorkout.workout == currentEditable.workout))
        println(".items : " + (originalWorkout.items == currentEditable.items))
        originalWorkout.items.forEachIndexed { index, orig ->
            val curr = currentEditable.items.getOrNull(index)
            if (orig != curr) {
                println("Not equal at position: $index")
                println("orig: $orig")
                println("curr: $curr")
            }
        }
        _state.update {
            it.copy(
                isDirty = currentEditable.isDirty(originalWorkout),
                workout = currentEditable,
                editableWorkout = null,
            )
        }
    }

    private fun resetWorkoutEditor() {
        _state.update {
            it.copy(editableWorkout = it.workout?.copy())
        }
    }

    private fun exercisePickedForWorkoutEditor(exerciseId: ExerciseId?) {
        if (exerciseId == null) return
        viewModelScope.launch {
            val exercise = repository.getExercise(exerciseId)
            val workoutExercise = WorkoutExercise(
                exerciseId,
                Quantity(exercise.quantityType, exercise.defaultQuantityValue)
            )
            val exerciseUiItem: ExerciseUiItem = workoutExercise.toUiBase(exercise)
            onEditorAction(WorkoutEditorAction.ExercisePicked(exerciseUiItem))
        }
    }

}