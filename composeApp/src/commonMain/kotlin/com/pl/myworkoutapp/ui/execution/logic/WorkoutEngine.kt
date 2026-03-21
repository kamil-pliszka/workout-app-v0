package com.pl.myworkoutapp.ui.execution.logic

import com.pl.myworkoutapp.ui.execution.WorkoutExecutionState
import kotlinx.coroutines.flow.StateFlow

sealed interface Action {
    object Start
    object Next
    object Pause
    object Resume
}

interface WorkoutEngine {
    val state: StateFlow<WorkoutExecutionState>
    fun dispatch(action: Action)
}