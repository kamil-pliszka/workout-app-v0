package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

data class ExerciseUiConfig(
    val name: UiText,
    val desc: UiText,
    val image: DrawableResource,
)

fun BuiltInExerciseId.toUiConfig(): ExerciseUiConfig = when (this) {
    BuiltInExerciseId.JUMPING_JACKS -> ExerciseUiConfig(
        name = Res.string.exercise_jumping_jacks.asUiText(),
        desc = Res.string.exercise_jumping_jacks_desc.asUiText(),
        image = Res.drawable.ic_jumping_jacks,
    )
    BuiltInExerciseId.PUSH_UP -> ExerciseUiConfig(
        name = Res.string.exercise_push_up.asUiText(),
        desc = Res.string.exercise_push_up_desc.asUiText(),
        image = Res.drawable.ic_push_up,
    )
    BuiltInExerciseId.TRICEPS_DIPS_ON_CHAIR -> ExerciseUiConfig(
        name = Res.string.exercise_triceps_dips_on_chair.asUiText(),
        desc = Res.string.exercise_triceps_dips_on_chair_desc.asUiText(),
        image = Res.drawable.ic_triceps_dip_on_chair,
    )
    BuiltInExerciseId.PLANK -> ExerciseUiConfig(
        name = Res.string.exercise_plank.asUiText(),
        desc = Res.string.exercise_plank_desc.asUiText(),
        image = Res.drawable.ic_plank1,
    )
    BuiltInExerciseId.SIDE_PLANK_LEFT -> ExerciseUiConfig(
        name = Res.string.exercise_side_plank_left.asUiText(),
        desc = Res.string.exercise_side_plank_desc.asUiText(),
        image = Res.drawable.ic_side_plank_left,
    )
    BuiltInExerciseId.SIDE_PLANK_RIGHT -> ExerciseUiConfig(
        name = Res.string.exercise_side_plank_right.asUiText(),
        desc = Res.string.exercise_side_plank_desc.asUiText(),
        image = Res.drawable.ic_side_plank_right,
    )
    BuiltInExerciseId.SQUATS -> ExerciseUiConfig(
        name = Res.string.exercise_squats.asUiText(),
        desc = Res.string.exercise_squats_desc.asUiText(),
        image = Res.drawable.ic_squat,
    )
    BuiltInExerciseId.BENT_LEG_TWIST -> ExerciseUiConfig(
        name = Res.string.exercise_bent_leg_twist.asUiText(),
        desc = Res.string.exercise_bent_leg_twist_desc.asUiText(),
        image = Res.drawable.ic_bent_leg_twist,
    )
    BuiltInExerciseId.COBRA_STRETCH -> ExerciseUiConfig(
        name = Res.string.exercise_cobra_stretch.asUiText(),
        desc = Res.string.exercise_cobra_stretch_desc.asUiText(),
        image = Res.drawable.ic_cobra_stretch,
    )
    BuiltInExerciseId.RUSSIAN_TWIST -> ExerciseUiConfig(
        name = Res.string.exercise_russian_twist.asUiText(),
        desc = Res.string.exercise_russian_twist_desc.asUiText(),
        image = Res.drawable.ic_russian_twist,
    )
    BuiltInExerciseId.FLUTTER_KICKS -> ExerciseUiConfig(
        name = Res.string.exercise_flutter_kicks.asUiText(),
        desc = Res.string.exercise_flutter_kicks_desc.asUiText(),
        image = Res.drawable.ic_flutter_kicks,
    )
    BuiltInExerciseId.REVERSE_CRUNCHES -> ExerciseUiConfig(
        name = Res.string.exercise_reverse_crunches.asUiText(),
        desc = Res.string.exercise_reverse_crunches_desc.asUiText(),
        image = Res.drawable.ic_reverse_crunches,
    )
    BuiltInExerciseId.HEEL_TOUCH -> ExerciseUiConfig(
        name = Res.string.exercise_heel_touch.asUiText(),
        desc = Res.string.exercise_heel_touch_desc.asUiText(),
        image = Res.drawable.ic_heel_touch,
    )
    BuiltInExerciseId.DEAD_BUG -> ExerciseUiConfig(
        name = Res.string.exercise_dead_bug.asUiText(),
        desc = Res.string.exercise_dead_bug_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_dead_bug,
    )
    BuiltInExerciseId.SUPERMAN -> ExerciseUiConfig(
        name = Res.string.exercise_superman.asUiText(),
        desc = Res.string.exercise_superman_desc.asUiText(),
        image = Res.drawable.ic_superman,
    )
    BuiltInExerciseId.V_HOLD -> ExerciseUiConfig(
        name = Res.string.exercise_v_hold.asUiText(),
        desc = Res.string.exercise_v_hold_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_v_hold,
    )
    BuiltInExerciseId.BUTT_BRIDGE -> ExerciseUiConfig(
        name = Res.string.exercise_butt_bridge.asUiText(),
        desc = Res.string.exercise_butt_bridge_desc.asUiText(),
        image = Res.drawable.ic_butt_bridge,
    )
    BuiltInExerciseId.SIDE_CRUNCHES_LEFT -> ExerciseUiConfig(
        name = Res.string.exercise_side_crunches_left.asUiText(),
        desc = Res.string.exercise_side_crunches_desc.asUiText(),
        image = Res.drawable.ic_side_crunches_left,
    )
    BuiltInExerciseId.SIDE_CRUNCHES_RIGHT -> ExerciseUiConfig(
        name = Res.string.exercise_side_crunches_right.asUiText(),
        desc = Res.string.exercise_side_crunches_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_side_crunches_right,
    )
    BuiltInExerciseId.CHILD_POSE -> ExerciseUiConfig(
        name = Res.string.exercise_child_pose.asUiText(),
        desc = Res.string.exercise_child_pose_desc.asUiText(),
        image = Res.drawable.ic_child_pose,
    )
    BuiltInExerciseId.PUSH_UP_HOLD_DOWN -> ExerciseUiConfig(
        name = Res.string.exercise_push_up_hold_down.asUiText(),
        desc = Res.string.exercise_push_up_hold_down_desc.asUiText(),
        image = Res.drawable.ic_push_up_hold_down,
    )
    BuiltInExerciseId.HOLLOW_BODY -> ExerciseUiConfig(
        name = Res.string.exercise_hollow_body.asUiText(),
        desc = Res.string.exercise_hollow_body_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_hollow_body,
    )
    BuiltInExerciseId.RUNNING -> ExerciseUiConfig(
        name = Res.string.exercise_run.asUiText(),
        desc = Res.string.exercise_run_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_run,
    )
    BuiltInExerciseId.RUNNING_ON_TIME -> ExerciseUiConfig(
        name = Res.string.exercise_run_on_time.asUiText(),
        desc = Res.string.exercise_run_on_time_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_run_on_time,
    )
    BuiltInExerciseId.WALKING -> ExerciseUiConfig(
        name = Res.string.exercise_walk.asUiText(),
        desc = Res.string.exercise_walk_desc.asUiText(),
        image = Res.drawable.ic_todo, //TODO Res.drawable.ic_walk,
    )
    BuiltInExerciseId.DUMBELL_BICEPS_CURLS -> ExerciseUiConfig(
        name = Res.string.exercise_dumbell_biceps_curls.asUiText(),
        desc = Res.string.exercise_dumbell_biceps_curls_desc.asUiText(),
        image = Res.drawable.ic_dumbell_biceps_curls,
    )
    BuiltInExerciseId.PUSH_UP_HOLD -> ExerciseUiConfig(
        name = Res.string.exercise_push_up_hold.asUiText(),
        desc = Res.string.exercise_push_up_hold_desc.asUiText(),
        image = Res.drawable.ic_push_up_hold,
    )

}
