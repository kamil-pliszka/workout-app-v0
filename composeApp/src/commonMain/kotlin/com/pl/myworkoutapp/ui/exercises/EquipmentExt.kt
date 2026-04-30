package com.pl.myworkoutapp.ui.exercises

import com.pl.myworkoutapp.domain.model.exercise.Equipment
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

fun Equipment.asUiText(): UiText {
    return when (this) {
        Equipment.BODYWEIGHT -> Res.string.equipment_bodyweight.asUiText()
        Equipment.DUMBBELLS -> Res.string.equipment_dumbbells.asUiText()
        Equipment.BARBELL -> Res.string.equipment_barbell.asUiText()
        Equipment.KETTLEBELL -> Res.string.equipment_kettlebell.asUiText()
        Equipment.RESISTANCE_BANDS -> Res.string.equipment_resistance_bands.asUiText()
        Equipment.MACHINE -> Res.string.equipment_machine.asUiText()
        Equipment.ROPE -> Res.string.equipment_rope.asUiText()
        Equipment.RINGS -> Res.string.equipment_rings.asUiText()
        Equipment.PULLUP_BAR -> Res.string.equipment_pullup_bar.asUiText()
    }
}


fun Equipment.getImageResource(): DrawableResource = when (this) {
    Equipment.BODYWEIGHT -> Res.drawable.ic_equipment_bodyweight
    Equipment.DUMBBELLS -> Res.drawable.ic_equipment_dumbbells
    Equipment.BARBELL -> Res.drawable.ic_equipment_barbell
    Equipment.KETTLEBELL -> Res.drawable.ic_equipment_kettlebell
    Equipment.RESISTANCE_BANDS -> Res.drawable.ic_equipment_resistance_bands
    Equipment.MACHINE -> Res.drawable.ic_equipment_machine
    Equipment.ROPE -> Res.drawable.ic_equipment_rope
    Equipment.RINGS -> Res.drawable.ic_equipment_rings
    Equipment.PULLUP_BAR -> Res.drawable.ic_equipment_pullup_bar
}
