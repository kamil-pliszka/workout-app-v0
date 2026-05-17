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
) {
    private val _state = MutableStateFlow<WorkoutExecutionRuntime?>(null)
    val state = _state.asStateFlow()

    private val _effects = Channel<ExecutionEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var timerJob: Job? = null

    fun start(
        initial: WorkoutExecutionRuntime,
        scope: CoroutineScope
    ) {
        stop()
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
        timerJob?.cancel()
        timerJob = null
        _state.value = null
    }

    fun dispatch(action: ExecutionAction) {
        val current = _state.value ?: return
        val newState = reducer.reduce(current, action)
        _state.value = newState

        val oldKeepOn = shouldKeepScreenOn(current.phase)
        val newKeepOn = shouldKeepScreenOn(newState.phase)

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

    private fun shouldKeepScreenOn(
        phase: ExecutionPhase
    ): Boolean {
        return phase in setOf(
            ExecutionPhase.Intro,
            ExecutionPhase.Exercise,
            ExecutionPhase.Rest
        )
    }
}