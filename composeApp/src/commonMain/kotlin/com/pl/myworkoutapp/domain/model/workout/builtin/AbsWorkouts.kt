package com.pl.myworkoutapp.domain.model.workout.builtin

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId


object AbsWorkouts {
    val MY_WORKOUT_NO_SET = BuiltInWorkout(
        id = BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = listOf(
            //WARMUP
            BuiltInExerciseId.JUMPING_JACKS.withDuration(30),
            BuiltInExerciseId.BENT_LEG_TWIST.withRepsPerSide(10),
            BuiltInExerciseId.COBRA_STRETCH.withDuration(30),

            //SET1
            BuiltInExerciseId.RUSSIAN_TWIST.withRepsPerSide(16),
            BuiltInExerciseId.FLUTTER_KICKS.withDuration(45),
            BuiltInExerciseId.REVERSE_CRUNCHES.withReps(30),
            BuiltInExerciseId.HEEL_TOUCH.withRepsPerSide(15),
            BuiltInExerciseId.DEAD_BUG.withRepsPerSide(20),
            BuiltInExerciseId.SUPERMAN.withReps(15),
            BuiltInExerciseId.V_HOLD.withDuration(40),
            BuiltInExerciseId.BUTT_BRIDGE.withReps(20),
            BuiltInExerciseId.SIDE_CRUNCHES_LEFT.withReps(20),
            BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.withReps(20),
            BuiltInExerciseId.PLANK.withDuration(120),
            BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
            BuiltInExerciseId.HOLLOW_BODY.withDuration(60),
            BuiltInExerciseId.CHILD_POSE.withDuration(30),

            //SET2
            BuiltInExerciseId.RUSSIAN_TWIST.withRepsPerSide(15),
            BuiltInExerciseId.FLUTTER_KICKS.withDuration(40),
            BuiltInExerciseId.REVERSE_CRUNCHES.withReps(30),
            BuiltInExerciseId.HEEL_TOUCH.withRepsPerSide(15),
            BuiltInExerciseId.DEAD_BUG.withRepsPerSide(20),
            BuiltInExerciseId.SUPERMAN.withReps(15),
            BuiltInExerciseId.V_HOLD.withDuration(40),
            BuiltInExerciseId.BUTT_BRIDGE.withReps(20),
            BuiltInExerciseId.SIDE_CRUNCHES_LEFT.withReps(20),
            BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.withReps(20),
            BuiltInExerciseId.SIDE_PLANK_LEFT.withDuration(100),
            BuiltInExerciseId.SIDE_PLANK_RIGHT.withDuration(100),
            BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
            BuiltInExerciseId.HOLLOW_BODY.withDuration(60),
            BuiltInExerciseId.CHILD_POSE.withDuration(30),

            //COOLDOWN
            BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
        )
    )

    val MY_ABS_WORKOUT_WITH_SET = BuiltInWorkout(
        id = BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = listOf(
            //WARMUP
            Circuit(
                phase = Phase.WARMUP,
                name = "warm-up",
                rounds = 1,
                items = listOf(
                    BuiltInExerciseId.JUMPING_JACKS.withDuration(30),
                    BuiltInExerciseId.BENT_LEG_TWIST.withRepsPerSide(10),
                    BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
                )
            ),
            //SETs
            Circuit(
                phase = Phase.MAIN,
                name = "abs set",
                rounds = 2,
                items = listOf(
                    BuiltInExerciseId.RUSSIAN_TWIST.withRepsPerSide(16),
                    BuiltInExerciseId.FLUTTER_KICKS.withDuration(45),
                    BuiltInExerciseId.REVERSE_CRUNCHES.withReps(30),
                    BuiltInExerciseId.HEEL_TOUCH.withRepsPerSide(15),
                    BuiltInExerciseId.DEAD_BUG.withRepsPerSide(20),
                    BuiltInExerciseId.SUPERMAN.withReps(15),
                    BuiltInExerciseId.V_HOLD.withDuration(40),
                    BuiltInExerciseId.BUTT_BRIDGE.withReps(20),
                    BuiltInExerciseId.SIDE_CRUNCHES_LEFT.withReps(20),
                    BuiltInExerciseId.SIDE_CRUNCHES_RIGHT.withReps(20),
                    BuiltInExerciseId.PLANK.withDuration(120),
                    BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
                    BuiltInExerciseId.HOLLOW_BODY.withDuration(60),
                    BuiltInExerciseId.CHILD_POSE.withDuration(30),
                )
            ),
            Circuit(
                phase = Phase.COOLDOWN,
                name = "cool-down",
                rounds = 1,
                items = listOf(
                    BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
                )
            ),
        ),
    )

    val MY_ABS_WORKOUT_SUPERSET = BuiltInWorkout(
        id = BuiltInWorkoutId.MY_ABS_WORKOUT_SUPERSET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = listOf(
            //WARMUP
            Circuit(
                phase = Phase.WARMUP,
                name = "warm-up",
                rounds = 1,
                items = listOf(
                    BuiltInExerciseId.JUMPING_JACKS.withDuration(30),
                    BuiltInExerciseId.BENT_LEG_TWIST.withRepsPerSide(10),
                    BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
                )
            ),
            //SETs
            Circuit(
                phase = Phase.MAIN,
                name = "super set",
                rounds = 2,
                items = listOf(
                    Circuit(
                        phase = Phase.MAIN,
                        name = "klata set",
                        rounds = 2,
                        items = listOf(
                            BuiltInExerciseId.PUSH_UP.withReps(15),
                            BuiltInExerciseId.V_HOLD.withDuration(40),
                        )
                    ),
                    Circuit(
                        phase = Phase.MAIN,
                        name = "brzuch set",
                        rounds = 1,
                        items = listOf(
                            BuiltInExerciseId.HEEL_TOUCH.withRepsPerSide(15),
                            BuiltInExerciseId.DEAD_BUG.withRepsPerSide(20),
                        )
                    ),
                    Circuit(
                        phase = Phase.MAIN,
                        name = "nogi set",
                        rounds = 3,
                        items = listOf(
                            BuiltInExerciseId.SQUATS.withReps(15),
                            BuiltInExerciseId.V_HOLD.withDuration(40),
                        )
                    ),
                ),
            ),
            Circuit(
                phase = Phase.COOLDOWN,
                name = "cool-down",
                rounds = 1,
                items = listOf(
                    BuiltInExerciseId.COBRA_STRETCH.withDuration(30),
                )
            ),
        ),
    )


    val SIX_PACK_10_MIN = BuiltInWorkout(
        id = BuiltInWorkoutId.SIX_PACK_10_MIN.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.PLANK.withDuration(20),
        ),
    )

    val SIX_PACK_20_MIN = BuiltInWorkout(
        id = BuiltInWorkoutId.SIX_PACK_20_MIN.asWorkoutId(),
        difficulty = Difficulty.INTERMEDIATE,
        items = listOf(
            BuiltInExerciseId.PLANK.withDuration(40),
        ),
    )

    //LEGS_AND_GLUTES_10_MIN


    fun ALL() = listOf(
        MY_WORKOUT_NO_SET,
        MY_ABS_WORKOUT_WITH_SET,
        MY_ABS_WORKOUT_SUPERSET,
        SIX_PACK_10_MIN,
        SIX_PACK_20_MIN,
    )
}