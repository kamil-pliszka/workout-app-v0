package com.pl.myworkoutapp.ui.execution.engine

/**
 * Aktualny tryb działania engine.
 * Opisuje:
 * w jakim stanie workflow obecnie jest runtime
 *
 * Przykłady:
 *  Exercise / Rest / Paused / Finished
 * To uproszczony high-level workflow state.
 */
sealed interface ExecutionPhase {
    data object Intro : ExecutionPhase
    data object Exercise : ExecutionPhase
    data object Rest : ExecutionPhase
    data object Paused : ExecutionPhase
    data object Finished : ExecutionPhase
}
