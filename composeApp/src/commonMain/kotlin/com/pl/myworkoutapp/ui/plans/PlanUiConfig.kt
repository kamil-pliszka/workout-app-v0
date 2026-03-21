package com.pl.myworkoutapp.ui.plans

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlanId
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.theme.Blue
import com.pl.myworkoutapp.ui.theme.LightBlue
import com.pl.myworkoutapp.ui.theme.holoGreen
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_plank1
import myworkoutapplication.composeapp.generated.resources.plans_belly_fat_burn
import myworkoutapplication.composeapp.generated.resources.plans_rock_solid_abs
import myworkoutapplication.composeapp.generated.resources.plans_six_pack_abs
import org.jetbrains.compose.resources.DrawableResource

data class PlanUiConfig(
    val name: UiText,
    val desc: UiText,
    val image: DrawableResource,
    val color: Color
)

fun BuiltInTrainingPlanId.toUiConfig(): PlanUiConfig = when (this) {
    BuiltInTrainingPlanId.BELLY_FAT_BURN -> PlanUiConfig(
        name = Res.string.plans_belly_fat_burn.asUiText(),
        desc = Res.string.plans_belly_fat_burn.asUiText(),//TODO
        image = Res.drawable.ic_plank1,
        color = Blue
    )
    BuiltInTrainingPlanId.SIX_PACK_ABS -> PlanUiConfig(
        name = Res.string.plans_six_pack_abs.asUiText(),
        desc = Res.string.plans_six_pack_abs.asUiText(),//TODO
        image = Res.drawable.ic_plank1,
        color = holoGreen
    )
    BuiltInTrainingPlanId.ROCK_SOLID_ABS -> PlanUiConfig(
        name = Res.string.plans_rock_solid_abs.asUiText(),
        desc = Res.string.plans_rock_solid_abs.asUiText(),//TODO
        image = Res.drawable.ic_plank1,
        color = LightBlue
    )
}
