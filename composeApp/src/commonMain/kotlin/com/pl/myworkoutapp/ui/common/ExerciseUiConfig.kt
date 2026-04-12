package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class ExerciseUiConfig(
    val name: StringResource,
    val image: DrawableResource,
)

fun BuiltInExerciseId.toUiConfig(): ExerciseUiConfig = ExerciseUiConfig(
    name = this.getNameResource(),
    image = this.getImageResource(),
)

fun BuiltInExerciseId.getNameResource(): StringResource = when (this) {
    BuiltInExerciseId.JUMPING_JACKS -> Res.string.exercise_jumping_jacks
    BuiltInExerciseId.PUSH_UP -> Res.string.exercise_push_up
    BuiltInExerciseId.TRICEPS_DIPS_ON_CHAIR -> Res.string.exercise_triceps_dips_on_chair
    BuiltInExerciseId.PLANK -> Res.string.exercise_plank
    BuiltInExerciseId.SIDE_PLANK_LEFT -> Res.string.exercise_side_plank_left
    BuiltInExerciseId.SIDE_PLANK_RIGHT -> Res.string.exercise_side_plank_right
    BuiltInExerciseId.SQUATS -> Res.string.exercise_squats
    BuiltInExerciseId.BENT_LEG_TWIST -> Res.string.exercise_bent_leg_twist
    BuiltInExerciseId.COBRA_STRETCH -> Res.string.exercise_cobra_stretch
    BuiltInExerciseId.RUSSIAN_TWIST -> Res.string.exercise_russian_twist
    BuiltInExerciseId.FLUTTER_KICKS -> Res.string.exercise_flutter_kicks
    BuiltInExerciseId.REVERSE_CRUNCHES -> Res.string.exercise_reverse_crunches
    BuiltInExerciseId.HEEL_TOUCH -> Res.string.exercise_heel_touch
    BuiltInExerciseId.DEAD_BUG -> Res.string.exercise_dead_bug
    BuiltInExerciseId.SUPERMAN -> Res.string.exercise_superman
    BuiltInExerciseId.V_HOLD -> Res.string.exercise_v_hold
    BuiltInExerciseId.BUTT_BRIDGE -> Res.string.exercise_butt_bridge
    BuiltInExerciseId.SIDE_CRUNCHES_LEFT -> Res.string.exercise_side_crunches_left
    BuiltInExerciseId.SIDE_CRUNCHES_RIGHT -> Res.string.exercise_side_crunches_right
    BuiltInExerciseId.CHILD_POSE -> Res.string.exercise_child_pose
    BuiltInExerciseId.PUSH_UP_HOLD_DOWN -> Res.string.exercise_push_up_hold_down
    BuiltInExerciseId.HOLLOW_BODY -> Res.string.exercise_hollow_body
    BuiltInExerciseId.RUNNING -> Res.string.exercise_run
    BuiltInExerciseId.RUNNING_ON_TIME -> Res.string.exercise_run_on_time
    BuiltInExerciseId.WALKING -> Res.string.exercise_walk
    BuiltInExerciseId.DUMBELL_BICEPS_CURLS -> Res.string.exercise_dumbell_biceps_curls
    BuiltInExerciseId.PUSH_UP_HOLD -> Res.string.exercise_push_up_hold
}




fun BuiltInExerciseId.getImageResource(): DrawableResource = when (this) {
    BuiltInExerciseId.JUMPING_JACKS -> Res.drawable.ic_jumping_jacks
    BuiltInExerciseId.PUSH_UP -> Res.drawable.ic_push_up
    BuiltInExerciseId.TRICEPS_DIPS_ON_CHAIR ->Res.drawable.ic_triceps_dip_on_chair
    BuiltInExerciseId.PLANK -> Res.drawable.ic_plank1
    BuiltInExerciseId.SIDE_PLANK_LEFT -> Res.drawable.ic_side_plank_left
    BuiltInExerciseId.SIDE_PLANK_RIGHT -> Res.drawable.ic_side_plank_right
    BuiltInExerciseId.SQUATS -> Res.drawable.ic_squat
    BuiltInExerciseId.BENT_LEG_TWIST -> Res.drawable.ic_bent_leg_twist
    BuiltInExerciseId.COBRA_STRETCH -> Res.drawable.ic_cobra_stretch
    BuiltInExerciseId.RUSSIAN_TWIST -> Res.drawable.ic_russian_twist
    BuiltInExerciseId.FLUTTER_KICKS -> Res.drawable.ic_flutter_kicks
    BuiltInExerciseId.REVERSE_CRUNCHES -> Res.drawable.ic_reverse_crunches
    BuiltInExerciseId.HEEL_TOUCH -> Res.drawable.ic_heel_touch
    BuiltInExerciseId.DEAD_BUG -> Res.drawable.ic_dead_bug
    BuiltInExerciseId.SUPERMAN -> Res.drawable.ic_superman
    BuiltInExerciseId.V_HOLD -> Res.drawable.ic_v_hold
    BuiltInExerciseId.BUTT_BRIDGE -> Res.drawable.ic_butt_bridge
    BuiltInExerciseId.SIDE_CRUNCHES_LEFT -> Res.drawable.ic_side_crunches_left
    BuiltInExerciseId.SIDE_CRUNCHES_RIGHT -> Res.drawable.ic_side_crunches_right
    BuiltInExerciseId.CHILD_POSE -> Res.drawable.ic_child_pose
    BuiltInExerciseId.PUSH_UP_HOLD_DOWN -> Res.drawable.ic_push_up_hold_down
    BuiltInExerciseId.HOLLOW_BODY -> Res.drawable.ic_hollow_body
    BuiltInExerciseId.RUNNING -> Res.drawable.ic_running
    BuiltInExerciseId.RUNNING_ON_TIME -> Res.drawable.ic_running
    BuiltInExerciseId.WALKING -> Res.drawable.ic_walking
    BuiltInExerciseId.DUMBELL_BICEPS_CURLS -> Res.drawable.ic_dumbell_biceps_curls
    BuiltInExerciseId.PUSH_UP_HOLD -> Res.drawable.ic_push_up_hold
}
