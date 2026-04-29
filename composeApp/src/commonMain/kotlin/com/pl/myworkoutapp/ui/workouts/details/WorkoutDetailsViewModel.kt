package com.pl.myworkoutapp.ui.workouts.details

import androidx.lifecycle.*
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.core.exceptionToString
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.toWorkoutIdOrNull
import com.pl.myworkoutapp.domain.usecase.*
import com.pl.myworkoutapp.ui.app.AppStateHolder
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import com.pl.myworkoutapp.ui.workouts.*
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditAction.ExerciseReplaced
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditAction.SelectedExerciseLoaded
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditAction.ShowLoadedExerciseInfo
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditorEvent.ScrollEditorTo
import com.pl.myworkoutapp.ui.workouts.tree.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.*

//Docelowa odpowiedzialność ViewModel
//WorkoutDetailsViewModel ma teraz tylko 4 obowiązki:
//1. Load initial state
//load workout
//transform
//wejście w Mode.View
//2. Route action
//WorkoutDetailsAction → WorkoutViewReducer albo WorkoutEditReducer
//3. Execute effects
//reducer zwraca effect
//VM wykonuje async / repo / usecase / nav
//4. Emit events
//snackbar
//close
//nav

class WorkoutDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val appStateHolder: AppStateHolder,
    private val saveWorkoutUseCase: SaveWorkoutUseCase,
    private val getWorkoutWithExercisesUseCase: GetWorkoutWithExercisesUseCase,
    private val getExerciseInfoUseCase: GetExerciseInfoUseCase,
    private val getExerciseWithDefaultQuantityUseCase: GetExerciseWithDefaultQuantityUseCase,
    private val deleteWorkoutUseCase: DeleteCustomWorkoutAndResolveFallbackUseCase,
    private val workoutViewReducer: WorkoutViewReducer,
    private val workoutEditReducer: WorkoutEditReducer,
    private val sessionCoordinator: WorkoutSessionCoordinator,
) : ViewModel() {

    private val workoutIdParam: String =
        savedStateHandle["workoutId"] ?: error("workoutId is required")

    private val _state = MutableStateFlow(
        WorkoutDetailsUiState(mode = WorkoutDetailsMode.Loading)
    )
    val state: StateFlow<WorkoutDetailsUiState> = _state

    private val _events = Channel<WorkoutDetailsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _editorEvents = Channel<WorkoutEditorEvent>(Channel.BUFFERED)
    val editorEvents = _editorEvents.receiveAsFlow()

    init {
        loadWorkoutFromParam()
    }

    // =========================================================
    // Public API
    // =========================================================

    fun onViewAction(action: WorkoutViewAction) {
        handleViewAction(action)
    }

    fun onEditAction(action: WorkoutEditAction) {
        handleEditAction(action)

    }

    fun onCircuitEditorAction(action: CircuitEditorAction) {
        val edit = _state.value.mode as? WorkoutDetailsMode.Edit ?: return

        val newSession = workoutEditReducer.reduce(edit.session, action)

        _state.update {
            it.copy(mode = edit.copy(session = newSession))
        }
    }

    // =========================================================
    // View mode
    // =========================================================


    private fun handleViewAction(action: WorkoutViewAction) {
        if (action is WorkoutViewAction.OnBack) {
            closeScreen()
            return
        }
        dispatchView(action)
    }

    private fun dispatchView(action: WorkoutViewAction) {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return
        val result = workoutViewReducer.reduce(view.session, action)
        applyViewResult(view, result)
    }

    private fun applyViewResult(
        current: WorkoutDetailsMode.View,
        result: WorkoutViewResult
    ) {
        _state.update {
            it.copy(mode = current.copy(session = result.state))
        }

        result.effect?.let { effect ->
            viewModelScope.launch {
                handleViewEffect(effect)
            }
        }
    }

    private suspend fun handleViewEffect(effect: WorkoutViewEffect) {
        when (effect) {
            is WorkoutViewEffect.LoadExerciseInfo -> {
                val info = getExerciseInfoUseCase.execute(effect.exerciseId).toUi()
                dispatchView(
                    WorkoutViewAction.ShowLoadedExerciseInfo(effect.key, info)
                )
            }

            is WorkoutViewEffect.ExchangeExercise -> {
                val info = getExerciseInfoUseCase.execute(effect.exerciseId).toUi()
                dispatchView(WorkoutViewAction.ExerciseReplaced(info))
            }

            WorkoutViewEffect.SaveWorkout -> saveWorkout()
            WorkoutViewEffect.ResetWorkout -> resetWorkout()
            WorkoutViewEffect.StartWorkout -> startWorkout()
            WorkoutViewEffect.OpenEditor -> openEditor()
            WorkoutViewEffect.DeleteWorkout -> deleteWorkout()
        }
    }

    // =========================================================
    // Edit mode
    // =========================================================

    private fun handleEditAction(action: WorkoutEditAction) {
        dispatchEdit(action)
    }

    private fun dispatchEdit(action: WorkoutEditAction) {
        val edit = _state.value.mode as? WorkoutDetailsMode.Edit ?: return
        val result = workoutEditReducer.reduce(edit.session, action)
        applyEditResult(edit, result)
    }

    private fun applyEditResult(
        current: WorkoutDetailsMode.Edit,
        result: WorkoutEditResult
    ) {
        _state.update {
            it.copy(mode = current.copy(session = result.state))
        }

        result.effect?.let { effect ->
            viewModelScope.launch {
                handleEditEffect(effect)
            }
        }
    }

    private suspend fun handleEditEffect(effect: WorkoutEditEffect) {
        when (effect) {
            is WorkoutEditEffect.LoadExerciseInfo -> {
                val info = getExerciseInfoUseCase.execute(effect.exerciseId).toUi()
                dispatchEdit(
                    ShowLoadedExerciseInfo(effect.key, info)
                )
            }

            is WorkoutEditEffect.LoadExerciseForList -> {
                dispatchEdit(
                    SelectedExerciseLoaded(
                        effect.context,
                        loadExeWithDefaultQuantity(effect.exerciseId)
                    )
                )
            }

            is WorkoutEditEffect.LoadExerciseForPreview -> {
                val info = getExerciseInfoUseCase.execute(effect.exerciseId).toUi()
                dispatchEdit(ExerciseReplaced(info))
            }

            WorkoutEditEffect.SaveDraft -> saveEditor()
            WorkoutEditEffect.ResetDraft -> resetEditor()
            WorkoutEditEffect.CloseEditor -> closeEditor()
            is WorkoutEditEffect.ScrollTo -> {
                _editorEvents.send(ScrollEditorTo(effect.index))
            }

            WorkoutEditEffect.Vibration -> {
                //TODO - przesłać gdzieś dalej ten event i zrobić wibracje na telefonie
            }
        }
    }

    private suspend fun loadExeWithDefaultQuantity(exerciseId: ExerciseId): ExerciseUiItem {
        val result = getExerciseWithDefaultQuantityUseCase.execute(exerciseId)
        return result.workoutExercise.toUiBase(result.exercise)
    }

    // =========================================================
    // Load
    // =========================================================

    private fun loadWorkoutFromParam() {
        val workoutId = workoutIdParam.toWorkoutIdOrNull()
        if (workoutId == null) {
            viewModelScope.launch {
                sendEvent(WorkoutDetailsEvent.ShowError("Cannot parse workoutId: $workoutIdParam".asUiText()))
                sendEvent(WorkoutDetailsEvent.Close)
            }
            return
        }
        when (workoutId) {
            WorkoutId.Custom.NEW -> prepareNewWorkout()
            else -> loadWorkoutById(workoutId)
        }
    }

    private fun prepareNewWorkout() {
        _state.update {
            it.copy(
                mode = WorkoutDetailsMode.View(
                    session = WorkoutViewSession(
                        workout = WorkoutWithExercisesUiModel(
                            //TODO - gdzieś to wynieść
                            workout = WorkoutUiModel(
                                workoutId = WorkoutId.Custom.NEW,
                                basedOn = null,
                                name = EmptyUiText,
                                desc = EmptyUiText,
                                imageUrl = Res.drawable.ic_flying_witch1,
                                isInProgress = false,
                                difficulty = Difficulty.ADVANCED,
                                themeColor = PearlOpalGreen,
                                durationText = EmptyUiText,
                                kcalText = EmptyUiText,
                            ),
                            items = emptyList()
                        ),
                        hasUnsavedChanges = true,
                    ),
                )
            )
        }
    }

    private fun loadWorkoutById(workoutId: WorkoutId) {
        viewModelScope.launch {
            _state.update { it.copy(mode = WorkoutDetailsMode.Loading) }

            try {
                val result = getWorkoutWithExercisesUseCase.execute(workoutId)
                val uiWorkout = transform(result.workout, result.exercises)

                _state.update {
                    it.copy(
                        mode = WorkoutDetailsMode.View(
                            session = WorkoutViewSession(workout = uiWorkout)
                        )
                    )
                }
            } catch (e: Throwable) {
                Log.e("WorkoutDetails", "Failed to load workout", e)
                showExceptionAsMessage(e)
                sendEvent(WorkoutDetailsEvent.Close)
            }
        }
    }

    // =========================================================
    // Effects impl
    // =========================================================

    private suspend fun saveWorkout() {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return
        val workout = view.session.workout
        val savedWorkoutId = saveWorkoutUseCase.execute(
            workoutId = workout.workout.workoutId,
            basedOn = workout.workout.basedOn,
            difficulty = workout.workout.difficulty,
            //items = toDomain(workout.items)
            items = workout.items.toTree().toDomain()
        )

        sendEvent(WorkoutDetailsEvent.ShowSuccess(Res.string.workout_saved_success.asUiText()))
        loadWorkoutById(savedWorkoutId)
    }

    private suspend fun deleteWorkout() {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return
        val workout = view.session.workout
        val fallbackWorkoudId = deleteWorkoutUseCase.execute(workout.workout.workoutId)
        //pokazać komunikat o udanym usunięciu
        sendEvent(WorkoutDetailsEvent.ShowSuccess(Res.string.workout_delete_success.asUiText()))
        if (fallbackWorkoudId != null) {
            loadWorkoutById(fallbackWorkoudId)
        } else {
            //tutaj raczej nie wystąpi taki przypadek, możliwe jedynie gdyby był to CustomWorkout ale bez ustawionego basedOn
            closeScreen()
        }
    }


    private fun resetWorkout() {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return
        loadWorkoutById(view.session.workout.workout.workoutId)
    }

    private suspend fun startWorkout() {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return

        if (view.session.hasUnsavedChanges) {
            sendEvent(WorkoutDetailsEvent.ShowError("Workout has unsaved changes".asUiText()))
            return
        }

        sendEvent(
            WorkoutDetailsEvent.NavToWorkoutExecution(
                view.session.workout.workout.workoutId
            )
        )
    }

    private fun openEditor() {
        val view = _state.value.mode as? WorkoutDetailsMode.View ?: return

        appStateHolder.setWorkoutEditorActive(true)//ukrycie bottom nav bar

        _state.update {
            it.copy(
                mode = WorkoutDetailsMode.Edit(
                    session = sessionCoordinator.openEditor(view.session)
                )
            )
        }
    }

    private fun saveEditor() {
        val edit = _state.value.mode as? WorkoutDetailsMode.Edit ?: return

        appStateHolder.setWorkoutEditorActive(false)//pokazanie bottom nav bar

        _state.update {
            it.copy(
                mode = WorkoutDetailsMode.View(
                    session = sessionCoordinator.saveEditor(edit.session)
                )
            )
        }
    }

    private fun closeEditor() {
        val edit = _state.value.mode as? WorkoutDetailsMode.Edit ?: return

        appStateHolder.setWorkoutEditorActive(false)//pokazanie bottom nav bar

        _state.update {
            it.copy(
                mode = WorkoutDetailsMode.View(
                    session = sessionCoordinator.closeEditor(edit.session)
                )
            )
        }
    }

    private fun resetEditor() {
        val edit = _state.value.mode as? WorkoutDetailsMode.Edit ?: return

        _state.update {
            it.copy(
                mode = edit.copy(
                    session = sessionCoordinator.resetEditor(edit.session)
                )
            )
        }
    }

    private fun closeScreen() {
        viewModelScope.launch {
            sendEvent(WorkoutDetailsEvent.Close)
        }
    }

    // =========================================================
    // Helpers
    // =========================================================

    private suspend fun sendEvent(event: WorkoutDetailsEvent) {
        _events.send(event)
    }

    private suspend fun showExceptionAsMessage(e: Throwable) {
        sendEvent(
            WorkoutDetailsEvent.ShowError(
                Res.string.error_during_processing.asUiText(exceptionToString(e))
            )
        )
    }
}