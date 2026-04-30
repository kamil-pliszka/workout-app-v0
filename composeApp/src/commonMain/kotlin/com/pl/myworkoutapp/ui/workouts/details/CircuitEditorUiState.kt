package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.workouts.*


data class CircuitEditorUiState(
    val isNew: Boolean,
    val isValid: Boolean = true,
    val phase: Phase = Phase.MAIN,
    val name: UiText = EmptyUiText,
    val structureType: CircuitStructureType = CircuitStructureType.Standard,

    val rounds: String = "3",

    val emomMinutes: String = "10",
    val amrapMinutes: String = "10",
    val tabataRounds: String = "5",
    val tabataWorkSec: String = "20",
    val tabataRestSec: String = "10",
)

fun CircuitEditorUiState.toStructure(): CircuitUiStructure = when (structureType) {
    CircuitStructureType.Standard -> structureStandard(
        rounds.toInt()
    )

    CircuitStructureType.EMOM -> structureEMOM(
        emomMinutes.toInt()
    )

    CircuitStructureType.AMRAP -> structureAMRAP(
        amrapMinutes.toInt()
    )

    CircuitStructureType.Tabata -> structureTabata(
        tabataRounds.toInt(),
        tabataWorkSec.toInt(),
        tabataRestSec.toInt()
    )
}

fun CircuitUiItem.toCircuitEditorUiState(): CircuitEditorUiState = CircuitEditorUiState(
    isNew = false,
    phase = phase,
    name = title,
    structureType = structure.type,
    rounds = if (structure.type == CircuitStructureType.Standard) structure.rounds.toString() else "",
    emomMinutes = if (structure.type == CircuitStructureType.EMOM) structure.emomMinutes.toString() else "",
    amrapMinutes = if (structure.type == CircuitStructureType.AMRAP) structure.amrapMinutes.toString() else "",
    tabataRounds = if (structure.type == CircuitStructureType.Tabata) structure.rounds.toString() else "",
    tabataWorkSec = if (structure.type == CircuitStructureType.Tabata) structure.tabataWorkSec.toString() else "",
    tabataRestSec = if (structure.type == CircuitStructureType.Tabata) structure.tabataRestSec.toString() else "",
)

fun CircuitEditorUiState.toCircuitUiItem(): CircuitUiItem = CircuitUiItem(
    phase = phase,
    structure = toStructure(),
    title = name,
)