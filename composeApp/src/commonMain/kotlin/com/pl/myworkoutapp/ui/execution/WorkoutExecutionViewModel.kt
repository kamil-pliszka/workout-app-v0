package com.pl.myworkoutapp.ui.execution

import androidx.lifecycle.*
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.core.exceptionToString
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.model.plan.PlanId
import com.pl.myworkoutapp.domain.model.plan.toPlanIdOrNull
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.domain.model.workout.toWorkoutIdOrNull
import com.pl.myworkoutapp.domain.usecase.PrepareWorkoutExecutionUseCase
import com.pl.myworkoutapp.ui.app.AppStateHolder
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.execution.engine.*
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.error_during_processing
import kotlin.coroutines.cancellation.CancellationException

/**
 * Warstwa orchestration/UI integration.
 *
 * Odpowiada za:
 *
 * ładowanie workoutu (PrepareWorkoutExecutionUseCase)
 * start engine
 * mapowanie engine.state -> WorkoutExecutionUiState
 * obsługę lifecycle screena (OnScreenEntered, OnScreenExited)
 * wysyłanie eventów UI (Close, ShowError, Vibrate)
 * integrację z platformą (AppNavigator, PlatformEffects, AppStateHolder)
 *
 * Nie powinien:
 *
 * implementować logiki wykonywania treningu
 * znać przejść stepów
 * odliczać czasu
 * mieć when(state) z logiką workoutu
 */
class WorkoutExecutionViewModel(
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val appStateHolder: AppStateHolder,
    private val appSettingRepository: AppSettingRepository,
    private val engine: WorkoutExecutionEngine,//engine jest stanowy, nie jest singletonem
    private val executionEffectHandler: ExecutionEffectHandler,
    private val persistenceHandler: ExecutionEventHandler,
    private val prepareWorkoutExecutionUC: PrepareWorkoutExecutionUseCase,
    private val executionRuntimeFactory: WorkoutExecutionRuntimeFactory
) : ViewModel() {

    @Suppress("PrivatePropertyName")
    private val TAG = "WorkoutExecutionVM"
    private val workoutIdParam: String =
        savedStateHandle["workoutId"] ?: error("workoutId is required")
    private val planIdParam: String? = savedStateHandle["planId"] //planId jest opcjonalny

    val state: StateFlow<WorkoutExecutionUiState> =
        engine.state
            .map {
                it?.toUiState() ?: WorkoutExecutionUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = WorkoutExecutionUiState.Loading
            )
    private val _events = Channel<WorkoutExecutionEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        Log.d(TAG, "PARAMS: workoutId: $workoutIdParam, planId: $planIdParam")
        appStateHolder.setThemeColor(PearlOpalGreen)
        appStateHolder.setWorkoutActive(true)
        observeEngineEffects()
        observeEngineEvents()
        loadWorkoutFromParam()
    }

    override fun onCleared() {
        try {
            engine.stop()
        } finally {
            appStateHolder.setThemeColor(null)
            appStateHolder.setWorkoutActive(false)
        }
    }

    private fun observeEngineEffects() {
        viewModelScope.launch {
            engine.effects.collect { effect ->
                executionEffectHandler.handle(effect)
            }
        }
    }

    private fun observeEngineEvents() {
        viewModelScope.launch {
            engine.events.collect { event ->
                try {
                    persistenceHandler.handle(event)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    Log.e(TAG, "Persistence failed", e)
                    showExceptionAsMessage(e)
                    sendEvent(WorkoutExecutionEvent.Close)
                }
            }
        }
    }

    // =========================================================
    // Public API
    // =========================================================
    fun onAction(action: WorkoutExecutionAction) {
        when (action) {
            WorkoutExecutionAction.OnExit -> {
                engine.stop()
                appNavigator.closeDialog()
            }

            WorkoutExecutionAction.PauseClicked -> {
                dispatch(ExecutionAction.Pause)
            }

            WorkoutExecutionAction.ResumeClicked -> {
                dispatch(ExecutionAction.Resume)
            }

            WorkoutExecutionAction.SkipClicked -> {
                dispatch(ExecutionAction.Skip)
            }

            WorkoutExecutionAction.FinishExerciseClicked -> {
                dispatch(ExecutionAction.FinishExercise)
            }
        }
    }

    // =========================================================
    // Public API - END
    // =========================================================

    private fun loadWorkoutFromParam() {
        viewModelScope.launch {
            val workoutId = workoutIdParam.toWorkoutIdOrNull()
            if (workoutId == null) {
                sendEvent(WorkoutExecutionEvent.ShowError("Cannot parse workoutId: $workoutIdParam".asUiText()))
                sendEvent(WorkoutExecutionEvent.Close)
                return@launch
            }
            val planId = planIdParam?.toPlanIdOrNull()
            if (!planIdParam.isNullOrEmpty() && planId == null) {
                sendEvent(WorkoutExecutionEvent.ShowError("Cannot parse planId: $planIdParam".asUiText()))
                sendEvent(WorkoutExecutionEvent.Close)
                return@launch
            }
            loadWorkoutById(planId, workoutId)
        }
    }

    private suspend fun loadWorkoutById(planId: PlanId?, workoutId: WorkoutId) {
        try {
            val weightKg = appSettingRepository.weightFlow.first()
            val preparation = prepareWorkoutExecutionUC.execute(
                planId, workoutId, weightKg
            )
            val runtime = executionRuntimeFactory.create(
                preparation.workout,
                preparation.exercises,
                preparation.session,
                weightKg
            )
            engine.start(
                initial = runtime,
                scope = viewModelScope
            )

        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            Log.e(TAG, "Failed to prepare workout session", e)
            showExceptionAsMessage(e)
            sendEvent(WorkoutExecutionEvent.Close)
        }
    }

    // =========================================================
    // Helpers
    // =========================================================

    private suspend fun sendEvent(event: WorkoutExecutionEvent) {
        _events.send(event)
    }

    private suspend fun showExceptionAsMessage(e: Throwable) {
        sendEvent(
            WorkoutExecutionEvent.ShowError(
                Res.string.error_during_processing.asUiText(exceptionToString(e))
            )
        )
    }

    private fun dispatch(action: ExecutionAction) {
        engine.dispatch(action)
    }

}