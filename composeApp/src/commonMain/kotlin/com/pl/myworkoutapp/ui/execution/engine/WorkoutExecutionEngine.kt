package com.pl.myworkoutapp.ui.execution.engine

/**
 * Runtime workflow engine dla wykonywania treningu.
 * Odpowiada za:
 *
 * utrzymywanie runtime state workoutu
 * dispatch akcji (Pause, Resume, Skip, Tick)
 * zarządzanie timerem
 * przejścia między krokami
 * workflow wykonania treningu
 *
 * To jest:
 * state machine
 * runtime coordinator
 *
 * Nie jest:
 * domeną biznesową /  composablem / ViewModel-em
 */
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class WorkoutExecutionEngine(
    private val reducer: ExecutionReducer,
    private val timer: ExecutionTimer,
    private val effectResolver: ExecutionEffectResolver,
    private val eventResolver: ExecutionEventResolver,
) {
    private val _state = MutableStateFlow<WorkoutExecutionRuntime?>(null)
    val state = _state.asStateFlow()

    private val _effects = Channel<ExecutionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _events = Channel<ExecutionEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var timerJob: Job? = null

    fun start(
        initial: WorkoutExecutionRuntime,
        scope: CoroutineScope
    ) {
        timerJob?.cancel()
        _state.value = initial
        emitEffect(
            ExecutionEffect.KeepScreenOn(true)
        )

        timerJob = scope.launch {
            timer.ticker().collect {
                dispatch(ExecutionAction.Tick)
            }
        }
    }

    fun stop() {
        emitEffect(
            ExecutionEffect.KeepScreenOn(false)
        )
        timerJob?.cancel()
        timerJob = null
        _state.value = null
    }

    fun dispatch(action: ExecutionAction) {
        val current = _state.value ?: return
        val newState = reducer.reduce(current, action)
        _state.value = newState

        eventResolver.resolve(
            old = current,
            new = newState,
            action = action,
        ).forEach {
            emitEvent(it)
        }

        val oldKeepOn = shouldKeepScreenOn(current.state)
        val newKeepOn = shouldKeepScreenOn(newState.state)

        if (oldKeepOn != newKeepOn) {
            emitEffect(
                ExecutionEffect.KeepScreenOn(newKeepOn)
            )
        }

        val effects = effectResolver.resolve(
            old = current,
            action = action,
            new = newState
        )

        effects.forEach {
            emitEffect(it)
        }
    }

    private fun emitEffect(effect: ExecutionEffect) {
        println("Engine: emitEffect: $effect")
        _effects.trySend(effect)
    }

    private fun emitEvent(event: ExecutionEvent) {
        println("Engine: emitEvent: $event")
        _events.trySend(event)
    }

    private fun shouldKeepScreenOn(
        runtimeState: RuntimeState
    ): Boolean {
        return runtimeState is RunningState
    }
}