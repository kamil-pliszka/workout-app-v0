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
    data class TabataWorksecChanged(val value: String) : CircuitEditorAction
    data class TabataRestsecChanged(val value: String) : CircuitEditorAction
}

class CircuitEditorDelegate {
    fun reduce(
        state: CircuitEditorUiState,
        action: CircuitEditorAction
    ): CircuitEditorUiState {
        val newState = when (action) {
            is CircuitEditorAction.PhaseChanged -> state.copy(phase = action.phase)
            is CircuitEditorAction.NameChanged -> state.copy(name = action.value.asUiText())
            is CircuitEditorAction.StructureChanged -> state.copy(structureType = action.type)
            is CircuitEditorAction.RoundsChanged -> state.copy(rounds = action.value)
            is CircuitEditorAction.EmomMinutesChanged -> state.copy(emomMinutes = action.value)
            is CircuitEditorAction.AmrapMinutesChanged -> state.copy(amrapMinutes = action.value)
            is CircuitEditorAction.TabataWorksecChanged -> state.copy(tabataWorkSec = action.value)
            is CircuitEditorAction.TabataRestsecChanged -> state.copy(tabataRestSec = action.value)
        }

        return newState.copy(
            isValid = validate(newState)
        )
    }

    fun validate(state: CircuitEditorUiState): Boolean {
        return when (state.structureType) {
            CircuitStructureType.Standard ->
                state.rounds.toIntOrNull()?.let { it > 0 } == true

            CircuitStructureType.EMOM ->
                state.emomMinutes.toIntOrNull()?.let { it > 0 } == true

            CircuitStructureType.AMRAP ->
                state.amrapMinutes.toIntOrNull()?.let { it > 0 } == true

            CircuitStructureType.Tabata ->
                state.tabataWorkSec.toIntOrNull()?.let { it > 0 } == true &&
                        state.tabataRestSec.toIntOrNull()?.let { it > 0 } == true
        }
    }
}