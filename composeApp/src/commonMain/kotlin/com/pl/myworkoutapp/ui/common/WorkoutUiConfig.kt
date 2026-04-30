package com.pl.myworkoutapp.ui.common

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.ui.theme.*
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

data class WorkoutUiConfig(
    val name: UiText,
    val desc: UiText,
    val image: DrawableResource,
    val color: Color
)

fun BuiltInWorkoutId.toUiConfig(): WorkoutUiConfig = when (this) {
    BuiltInWorkoutId.SIX_PACK_10_MIN -> WorkoutUiConfig(
        name = Res.string.workouts_six_pack_10_minutes.asUiText(),
        desc = Res.string.workouts_six_pack_10_minutes_desc.asUiText(),
        image = Res.drawable.ic_jumping_jacks,
        color = DarkBlue
    )

    BuiltInWorkoutId.SIX_PACK_20_MIN -> WorkoutUiConfig(
        name = Res.string.plans_belly_fat_burn.asUiText(),
        desc = Res.string.plans_belly_fat_burn.asUiText(),
        image = Res.drawable.ic_side_plank,
        color = BrillantBlue
    )

    BuiltInWorkoutId.LEGS_AND_GLUTES_10_MIN -> WorkoutUiConfig(
        name = Res.string.workouts_legs_and_glutes_10_min.asUiText(),
        desc = Res.string.workouts_legs_and_glutes_10_min_desc.asUiText(),
        image = Res.drawable.ic_plank1,
        color = PastelTurquoise
    )

    BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET -> WorkoutUiConfig(
        name = Res.string.workouts_my_abs_workout_no_set.asUiText(),
        desc = Res.string.workouts_my_abs_workout_no_set_desc.asUiText(),
        image = Res.drawable.ic_triceps_dip_on_chair,
        color = PastelGreen
    )

    BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET -> WorkoutUiConfig(
        name = Res.string.workouts_my_abs_workout_with_set.asUiText(),
        desc = Res.string.workouts_my_abs_workout_with_set_desc.asUiText(),
        image = Res.drawable.ic_triceps_dip_on_chair,
        color = YellowGreen
    )

    BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET -> WorkoutUiConfig(
        name = Res.string.workouts_my_abs_workout_with_superset.asUiText(),
        desc = Res.string.workouts_my_abs_workout_with_superset_desc.asUiText(),
        image = Res.drawable.ic_plank1,
        color = SilverGrey
    )

    BuiltInWorkoutId.TABATA_1 -> WorkoutUiConfig(
        name = Res.string.workouts_tabata_1.asUiText(),
        desc = Res.string.workouts_tabata_1_desc.asUiText(),
        image = Res.drawable.ic_jumping_jacks,
        color = PureGreen
    )
}
