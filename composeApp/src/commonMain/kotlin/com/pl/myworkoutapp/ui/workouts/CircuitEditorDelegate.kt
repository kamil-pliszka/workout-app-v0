package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.asUiText

sealed interface CircuitEditorAction {
    data class PhaseChanged(val phase: Phase) : CircuitEditorAction
    data class NameChanged(val value: String) : CircuitEditorAction
    data class StructureChanged(val type: CircuitStructureType) : CircuitEditorAction
    data class RoundsChanged(val value: String) : CircuitEditorAction
    data class EmomMinutesChanged(val value: String) : CircuitEditorAction
    data class AmrapMinutesChanged(val value: String) : CircuitEditorAction
    data class TabataRoundsChanged(val value: String) : CircuitEditorAction
    data class TabataWorkSecChanged(val value: String) : CircuitEditorAction
    data class TabataRestSecChanged(val value: String) : CircuitEditorAction
}

class CircuitEditorDelegate {

    fun reduce(
        state: CircuitEditorUiState,
        action: CircuitEditorAction
    ): CircuitEditorUiState = when (action) {
        is CircuitEditorAction.PhaseChanged -> state.copy(phase = action.phase)
        is CircuitEditorAction.NameChanged -> state.copy(name = action.value.trim().asUiText())
        is CircuitEditorAction.StructureChanged -> state.copy(structureType = action.type)
        is CircuitEditorAction.RoundsChanged -> state.copy(rounds = action.value)
        is CircuitEditorAction.EmomMinutesChanged -> state.copy(emomMinutes = action.value)
        is CircuitEditorAction.AmrapMinutesChanged -> state.copy(amrapMinutes = action.value)
        is CircuitEditorAction.TabataRoundsChanged -> state.copy(tabataRounds = action.value)
        is CircuitEditorAction.TabataWorkSecChanged -> state.copy(tabataWorkSec = action.value)
        is CircuitEditorAction.TabataRestSecChanged -> state.copy(tabataRestSec = action.value)
    }.let { newState ->
        newState.copy(isValid = validate(newState))
    }

    fun validate(state: CircuitEditorUiState): Boolean = with(state) {
        when (structureType) {
            CircuitStructureType.Standard -> rounds.isPositiveInt()
            CircuitStructureType.EMOM -> emomMinutes.isPositiveInt()
            CircuitStructureType.AMRAP -> amrapMinutes.isPositiveInt()
            CircuitStructureType.Tabata -> listOf(
                tabataRounds,
                tabataWorkSec,
                tabataRestSec
            ).all { it.isPositiveInt() }
        }
    }

    private fun String.isPositiveInt(): Boolean =
        toIntOrNull()?.let { it > 0 } == true
}
