package com.pl.myworkoutapp.ui.workouts.details

import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
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

fun CircuitEditorUiState.toStructure() = when (structureType) {
    CircuitStructureType.Standard -> CircuitStructure.Standard(rounds.toInt())
    CircuitStructureType.EMOM -> CircuitStructure.EMOM(emomMinutes.toInt())
    CircuitStructureType.AMRAP -> CircuitStructure.AMRAP(amrapMinutes.toInt() * 60)
    CircuitStructureType.Tabata -> CircuitStructure.Tabata(
        rounds = tabataRounds.toInt(),
        workSec = tabataWorkSec.toInt(),
        restSec = tabataRestSec.toInt()
    )
}

fun CircuitUiItem.toCircuitEditorUiState() : CircuitEditorUiState = CircuitEditorUiState(
    isNew = false,
    phase = phase,
    name = title,
    structureType = structure.toStructureType(),
    rounds = if (structure is CircuitStructure.Standard) structure.rounds.toString() else "",
    emomMinutes = if (structure is CircuitStructure.EMOM) structure.minutes.toString() else "",
    amrapMinutes = if (structure is CircuitStructure.AMRAP) structure.durationSec.toString() else "",
    tabataRounds = if (structure is CircuitStructure.Tabata) structure.rounds.toString() else "",
    tabataWorkSec = if (structure is CircuitStructure.Tabata) structure.workSec.toString() else "",
    tabataRestSec = if (structure is CircuitStructure.Tabata) structure.restSec.toString() else "",
)

fun CircuitEditorUiState.toCircuitUiItem() : CircuitUiItem = CircuitUiItem(
    phase = phase,
    structure = toStructure(),
    title = name,
)