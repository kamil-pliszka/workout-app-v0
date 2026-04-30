package com.pl.myworkoutapp.ui.navigation

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface AppNavigatorEvent {
    data object PopBackStack : AppNavigatorEvent
    data class NavToExerciseEditor(val exerciseId: ExerciseId) : AppNavigatorEvent
    data class NavToWorkoutDetails(val workoutId: WorkoutId) : AppNavigatorEvent
    data class NavToWorkoutExecution(val workoutId: WorkoutId) : AppNavigatorEvent
}

class AppNavigator {
    private val _navEvents = MutableSharedFlow<AppNavigatorEvent>(
        extraBufferCapacity = 1
    )
    val navEvents = _navEvents.asSharedFlow()

    private fun tryEmitEvent(event: AppNavigatorEvent) {
        //_navEvents.tryEmit(event)
        check(_navEvents.tryEmit(event)) {
            "Failed to emit navigation event: $event"
        }
    }

    fun closeDialog() {
        tryEmitEvent(AppNavigatorEvent.PopBackStack)
    }

    fun navigateToExerciseEditor(exerciseId: ExerciseId) {
        tryEmitEvent(AppNavigatorEvent.NavToExerciseEditor(exerciseId))
    }

    fun navigateToWorkoutDetails(workoutId: WorkoutId) {
        tryEmitEvent(AppNavigatorEvent.NavToWorkoutDetails(workoutId))
    }

    fun navigateToWorkoutExecution(workoutId: WorkoutId) {
        tryEmitEvent(AppNavigatorEvent.NavToWorkoutExecution(workoutId))
    }

}

