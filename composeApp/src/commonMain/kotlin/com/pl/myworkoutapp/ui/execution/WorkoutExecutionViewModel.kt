package com.pl.myworkoutapp.ui.execution

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pl.myworkoutapp.ui.app.AppStateHolder
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkoutExecutionViewModel(
    private val effects: PlatformEffects,
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val appStateHolder: AppStateHolder,
) : ViewModel() {
    private val workoutId: String = savedStateHandle["workoutId"] ?: error("workoutId is required")

    init {
        println("WorkoutExecutionViewModel got workoutId: $workoutId")
    }

    //private val _effects = MutableSharedFlow<WorkoutEffect>()
    //val effects = _effects.asSharedFlow()

    private val _state = MutableStateFlow<WorkoutExecutionState>(
        WorkoutExecutionState.Paused(workoutId)
    )
    val state: StateFlow<WorkoutExecutionState> = _state

    private var isActive = false
    fun onScreenEntered() {
        if (!isActive) {
            isActive = true
            //appStateHolder.setHideNavigation(true)
            appStateHolder.setWorkoutActive(true)
            appStateHolder.setThemeColor(PearlOpalGreen)
        }
    }

    fun onScreenExited() {
        if (isActive) {
            isActive = false
            //appStateHolder.setHideNavigation(false)
            appStateHolder.setThemeColor(null)
            appStateHolder.setWorkoutActive(false)
        }
    }

    fun onAction(action: WorkoutExecutionAction) {
        println("Got action: $action")
        when (action) {
            WorkoutExecutionAction.OnScreenEntered -> onScreenEntered()
            WorkoutExecutionAction.OnScreenExited -> onScreenExited()
            WorkoutExecutionAction.OnExit -> {
                appNavigator.closeDialog()
            }
        }
    }


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
        _state.value = WorkoutExecutionState.Paused("after pause")
    }

    fun finish() {
        //_effects.emit(WorkoutEffect.AllowScreenOff)
        //_effects.emit(WorkoutEffect.PlaySound(SoundType.FINISH))
        effects.keepScreenOn(false)
    }
}