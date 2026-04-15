package com.pl.myworkoutapp.ui.execution

sealed interface WorkoutExecutionAction {
    object OnScreenEntered: WorkoutExecutionAction
    object OnScreenExited: WorkoutExecutionAction
    object OnExit: WorkoutExecutionAction //TODO pewnie bedzie jakas inna nazwa
}