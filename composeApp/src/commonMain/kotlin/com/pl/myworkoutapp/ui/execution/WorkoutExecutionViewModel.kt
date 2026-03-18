package com.pl.myworkoutapp.ui.execution

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkoutExecutionViewModel(
    private val effects: PlatformEffects,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val workoutId: String = savedStateHandle["workoutId"] ?: error("workoutId is required")

    init {
        println("WorkoutExecutionViewModel got workoutId: $workoutId")
    }

    //private val _effects = MutableSharedFlow<WorkoutEffect>()
    //val effects = _effects.asSharedFlow()

    private val _state = MutableStateFlow<WorkoutExecutionState>(
        WorkoutExecutionState.Paused
    )
    val state: StateFlow<WorkoutExecutionState> = _state

    // start pierwszego ćwiczenia
    fun startWorkout() {
        //_effects.emit(WorkoutEffect.KeepScreenOn)
        //_effects.emit(WorkoutEffect.PlaySound(SoundType.START))
        effects.keepScreenOn(true)
    }

    // przejście do kolejnego
    fun nextExercise() {
        //_effects.emit(WorkoutEffect.Vibrate(200))
        //_effects.emit(WorkoutEffect.PlaySound(SoundType.NEXT))
    }

    fun rest() {
        //_effects.emit(WorkoutEffect.Speak("Rest"))
    }

    fun resume() {
        // powrót do poprzedniego stanu
    }

    fun pause() {
        _state.value = WorkoutExecutionState.Paused
    }

    fun finish() {
        //_effects.emit(WorkoutEffect.AllowScreenOff)
        //_effects.emit(WorkoutEffect.PlaySound(SoundType.FINISH))
        effects.keepScreenOn(false)
    }
}