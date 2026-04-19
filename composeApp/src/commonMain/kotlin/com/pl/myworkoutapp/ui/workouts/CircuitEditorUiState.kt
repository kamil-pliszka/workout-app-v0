package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.UiText


data class CircuitEditorUiState(
    val isNew: Boolean,
    val isValid: Boolean = true,
    val phase: Phase = Phase.MAIN,
    val name: UiText = EmptyUiText,
    val structureType: CircuitStructureType = CircuitStructureType.Standard,

    val rounds: String = "1",

    val emomMinutes: String = "10",
    val amrapMinutes: String = "10",
    val tabataWorkSec: String = "20",
    val tabataRestSec: String = "10",
)

//fun CircuitEditorUiState.isValid(): Boolean {
//    return when (structureType) {
//        CircuitStructureType.Standard -> rounds.toIntOrNull()?.let { it > 0 } == true
//        CircuitStructureType.EMOM -> emomMinutes.toIntOrNull()?.let { it > 0 } == true
//        CircuitStructureType.AMRAP -> amrapMinutes.toIntOrNull()?.let { it > 0 } == true
//        CircuitStructureType.Tabata ->
//            tabataWorkSec.toIntOrNull()?.let { it > 0 } == true &&
//                    tabataRestSec.toIntOrNull()?.let { it > 0 } == true
//    }
//}

fun CircuitEditorUiState.toStructure() = when (structureType) {
    CircuitStructureType.Standard -> CircuitStructure.Standard
    CircuitStructureType.EMOM -> CircuitStructure.EMOM(emomMinutes.toInt())
    CircuitStructureType.AMRAP -> CircuitStructure.AMRAP(amrapMinutes.toInt() * 60)
    CircuitStructureType.Tabata -> CircuitStructure.Tabata(
        workSec = tabataWorkSec.toInt(),
        restSec = tabataRestSec.toInt()
    )
}

fun CircuitUiItem.toCircuitEditorUiState() : CircuitEditorUiState = CircuitEditorUiState(
    isNew = false,
    phase = phase,
    name = title,
    structureType = structure.toStructureType(),
    rounds = rounds.toString(),
    emomMinutes = if (structure is CircuitStructure.EMOM) structure.minutes.toString() else "",
    amrapMinutes = if (structure is CircuitStructure.AMRAP) structure.durationSec.toString() else "",
    tabataWorkSec = if (structure is CircuitStructure.Tabata) structure.workSec.toString() else "",
    tabataRestSec = if (structure is CircuitStructure.Tabata) structure.restSec.toString() else "",
)

fun CircuitEditorUiState.toCircuitUiItem() : CircuitUiItem = CircuitUiItem(
    phase = phase,
    rounds = rounds.toInt(),
    structure = toStructure(),
    title = name,
)