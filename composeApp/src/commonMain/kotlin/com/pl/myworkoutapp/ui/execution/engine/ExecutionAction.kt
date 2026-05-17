package com.pl.myworkoutapp.ui.execution.engine

/**
 * Intencje workflow.
 * Opisuje:
 * co chce zrobić user/system
 *
 * Przykłady:
 *  Pause / Resume / Tick / Skip / FinishExercise
 * To nie są eventy UI.
 * To input dla state machine.
 */
sealed interface ExecutionAction {
    data object Pause : ExecutionAction

    data object Resume : ExecutionAction

    data object Skip : ExecutionAction

    data object Tick : ExecutionAction

    data object FinishExercise : ExecutionAction
}
