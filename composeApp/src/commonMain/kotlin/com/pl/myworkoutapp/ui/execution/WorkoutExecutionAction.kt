package com.pl.myworkoutapp.ui.execution

sealed interface WorkoutExecutionAction {
    data object OnScreenEntered : WorkoutExecutionAction
    data object OnScreenExited : WorkoutExecutionAction
    data object OnExit : WorkoutExecutionAction
    data object PauseClicked : WorkoutExecutionAction
    data object ResumeClicked : WorkoutExecutionAction
    data object SkipClicked : WorkoutExecutionAction
    data object FinishExerciseClicked : WorkoutExecutionAction
}