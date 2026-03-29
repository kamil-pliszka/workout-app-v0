package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.exercise_bent_leg_twist
import myworkoutapplication.composeapp.generated.resources.exercise_butt_bridge
import myworkoutapplication.composeapp.generated.resources.exercise_child_pose
import myworkoutapplication.composeapp.generated.resources.exercise_cobra_stretch
import myworkoutapplication.composeapp.generated.resources.exercise_dead_bug
import myworkoutapplication.composeapp.generated.resources.exercise_dumbell_biceps_curls
import myworkoutapplication.composeapp.generated.resources.exercise_flutter_kicks
import myworkoutapplication.composeapp.generated.resources.exercise_heel_touch
import myworkoutapplication.composeapp.generated.resources.exercise_hollow_body
import myworkoutapplication.composeapp.generated.resources.exercise_jumping_jacks
import myworkoutapplication.composeapp.generated.resources.exercise_plank
import myworkoutapplication.composeapp.generated.resources.exercise_push_up
import myworkoutapplication.composeapp.generated.resources.exercise_push_up_hold
import myworkoutapplication.composeapp.generated.resources.exercise_push_up_hold_down
import myworkoutapplication.composeapp.generated.resources.exercise_reverse_crunches
import myworkoutapplication.composeapp.generated.resources.exercise_run
import myworkoutapplication.composeapp.generated.resources.exercise_run_on_time
import myworkoutapplication.composeapp.generated.resources.exercise_russian_twist
import myworkoutapplication.composeapp.generated.resources.exercise_side_crunches_left
import myworkoutapplication.composeapp.generated.resources.exercise_side_crunches_right
import myworkoutapplication.composeapp.generated.resources.exercise_side_plank_left
import myworkoutapplication.composeapp.generated.resources.exercise_side_plank_right
import myworkoutapplication.composeapp.generated.resources.exercise_squats
import myworkoutapplication.composeapp.generated.resources.exercise_superman
import myworkoutapplication.composeapp.generated.resources.exercise_triceps_dips_on_chair
import myworkoutapplication.composeapp.generated.resources.exercise_v_hold
import myworkoutapplication.composeapp.generated.resources.exercise_walk
import myworkoutapplication.composeapp.generated.resources.ic_bent_leg_twist
import myworkoutapplication.composeapp.generated.resources.ic_butt_bridge
import myworkoutapplication.composeapp.generated.resources.ic_child_pose
import myworkoutapplication.composeapp.generated.resources.ic_cobra_stretch
import myworkoutapplication.composeapp.generated.resources.ic_dead_bug
import myworkoutapplication.composeapp.generated.resources.ic_dumbell_biceps_curls
import myworkoutapplication.composeapp.generated.resources.ic_flutter_kicks
import myworkoutapplication.composeapp.generated.resources.ic_heel_touch
import myworkoutapplication.composeapp.generated.resources.ic_hollow_body
import myworkoutapplication.composeapp.generated.resources.ic_jumping_jacks
import myworkoutapplication.composeapp.generated.resources.ic_plank1
import myworkoutapplication.composeapp.generated.resources.ic_push_up
import myworkoutapplication.composeapp.generated.resources.ic_push_up_hold
import myworkoutapplication.composeapp.generated.resources.ic_push_up_hold_down
import myworkoutapplication.composeapp.generated.resources.ic_reverse_crunches
import myworkoutapplication.composeapp.generated.resources.ic_running
import myworkoutapplication.composeapp.generated.resources.ic_russian_twist
import myworkoutapplication.composeapp.generated.resources.ic_side_crunches_left
import myworkoutapplication.composeapp.generated.resources.ic_side_crunches_right
import myworkoutapplication.composeapp.generated.resources.ic_side_plank_left
import myworkoutapplication.composeapp.generated.resources.ic_side_plank_right
import myworkoutapplication.composeapp.generated.resources.ic_squat
import myworkoutapplication.composeapp.generated.resources.ic_superman
import myworkoutapplication.composeapp.generated.resources.ic_todo
import myworkoutapplication.composeapp.generated.resources.ic_triceps_dip_on_chair
import myworkoutapplication.composeapp.generated.resources.ic_v_hold
import myworkoutapplication.composeapp.generated.resources.ic_walking
import org.jetbrains.compose.resources.DrawableResource

data class ExerciseUiConfig(
    val name: UiText,
    val image: DrawableResource,
)

fun BuiltInExerciseId.toUiConfig(): ExerciseUiConfig = when (this) {
    BuiltInExerciseId.JUMPING_JACKS -> ExerciseUiConfig(
        name = Res.string.exercise_jumping_jacks.asUiText(),
        image = Res.drawable.ic_jumping_jacks,
    )
    BuiltInExerciseId.PUSH_UP -> ExerciseUiConfig(
        name = Res.string.exercise_push_up.asUiText(),
        image = Res.drawable.ic_push_up,
    )
    BuiltInExerciseId.TRICEPS_DIPS_ON_CHAIR -> ExerciseUiConfig(
        name = Res.string.exercise_triceps_dips_on_chair.asUiText(),
        image = Res.drawable.ic_triceps_dip_on_chair,
    )
    BuiltInExerciseId.PLANK -> ExerciseUiConfig(
        name = Res.string.exercise_plank.asUiText(),
        image = Res.drawable.ic_plank1,
    )
    BuiltInExerciseId.SIDE_PLANK_LEFT -> ExerciseUiConfig(
        name = Res.string.exercise_side_plank_left.asUiText(),
        image = Res.drawable.ic_side_plank_left,
    )
    BuiltInExerciseId.SIDE_PLANK_RIGHT -> ExerciseUiConfig(
        name = Res.string.exercise_side_plank_right.asUiText(),
        image = Res.drawable.ic_side_plank_right,
    )
    BuiltInExerciseId.SQUATS -> ExerciseUiConfig(
        name = Res.string.exercise_squats.asUiText(),
        image = Res.drawable.ic_squat,
    )
    BuiltInExerciseId.BENT_LEG_TWIST -> ExerciseUiConfig(
        name = Res.string.exercise_bent_leg_twist.asUiText(),
        image = Res.drawable.ic_bent_leg_twist,
    )
    BuiltInExerciseId.COBRA_STRETCH -> ExerciseUiConfig(
        name = Res.string.exercise_cobra_stretch.asUiText(),
        image = Res.drawable.ic_cobra_stretch,
    )
    BuiltInExerciseId.RUSSIAN_TWIST -> ExerciseUiConfig(
        name = Res.string.exercise_russian_twist.asUiText(),
        image = Res.drawable.ic_russian_twist,
    )
    BuiltInExerciseId.FLUTTER_KICKS -> ExerciseUiConfig(
        name = Res.string.exercise_flutter_kicks.asUiText(),
        image = Res.drawable.ic_flutter_kicks,
    )
    BuiltInExerciseId.REVERSE_CRUNCHES -> ExerciseUiConfig(
        name = Res.string.exercise_reverse_crunches.asUiText(),
        image = Res.drawable.ic_reverse_crunches,
    )
    BuiltInExerciseId.HEEL_TOUCH -> ExerciseUiConfig(
        name = Res.string.exercise_heel_touch.asUiText(),
        image = Res.drawable.ic_heel_touch,
    )
    BuiltInExerciseId.DEAD_BUG -> ExerciseUiConfig(
        name = Res.string.exercise_dead_bug.asUiText(),
        image = Res.drawable.ic_dead_bug,
    )
    BuiltInExerciseId.SUPERMAN -> ExerciseUiConfig(
        name = Res.string.exercise_superman.asUiText(),
        image = Res.drawable.ic_superman,
    )
    BuiltInExerciseId.V_HOLD -> ExerciseUiConfig(
        name = Res.string.exercise_v_hold.asUiText(),
        image = Res.drawable.ic_v_hold,
    )
    BuiltInExerciseId.BUTT_BRIDGE -> ExerciseUiConfig(
        name = Res.string.exercise_butt_bridge.asUiText(),
        image = Res.drawable.ic_butt_bridge,
    )
    BuiltInExerciseId.SIDE_CRUNCHES_LEFT -> ExerciseUiConfig(
        name = Res.string.exercise_side_crunches_left.asUiText(),
        image = Res.drawable.ic_side_crunches_left,
    )
    BuiltInExerciseId.SIDE_CRUNCHES_RIGHT -> ExerciseUiConfig(
        name = Res.string.exercise_side_crunches_right.asUiText(),
        image = Res.drawable.ic_side_crunches_right,
    )
    BuiltInExerciseId.CHILD_POSE -> ExerciseUiConfig(
        name = Res.string.exercise_child_pose.asUiText(),
        image = Res.drawable.ic_child_pose,
    )
    BuiltInExerciseId.PUSH_UP_HOLD_DOWN -> ExerciseUiConfig(
        name = Res.string.exercise_push_up_hold_down.asUiText(),
        image = Res.drawable.ic_push_up_hold_down,
    )
    BuiltInExerciseId.HOLLOW_BODY -> ExerciseUiConfig(
        name = Res.string.exercise_hollow_body.asUiText(),
        image = Res.drawable.ic_hollow_body,
    )
    BuiltInExerciseId.RUNNING -> ExerciseUiConfig(
        name = Res.string.exercise_run.asUiText(),
        image = Res.drawable.ic_running,
    )
    BuiltInExerciseId.RUNNING_ON_TIME -> ExerciseUiConfig(
        name = Res.string.exercise_run_on_time.asUiText(),
        image = Res.drawable.ic_running,
    )
    BuiltInExerciseId.WALKING -> ExerciseUiConfig(
        name = Res.string.exercise_walk.asUiText(),
        image = Res.drawable.ic_walking,
    )
    BuiltInExerciseId.DUMBELL_BICEPS_CURLS -> ExerciseUiConfig(
        name = Res.string.exercise_dumbell_biceps_curls.asUiText(),
        image = Res.drawable.ic_dumbell_biceps_curls,
    )
    BuiltInExerciseId.PUSH_UP_HOLD -> ExerciseUiConfig(
        name = Res.string.exercise_push_up_hold.asUiText(),
        image = Res.drawable.ic_push_up_hold,
    )

}
