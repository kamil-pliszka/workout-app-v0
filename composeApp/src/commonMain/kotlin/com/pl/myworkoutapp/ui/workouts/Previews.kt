package com.pl.myworkoutapp.ui.workouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_ABS_WORKOUT_WITH_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_WORKOUT_NO_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.withRepsPerSide
import com.pl.myworkoutapp.ui.workouts.components.WorkoutCard

//w głównej mierze chcemy tutaj przetestować wygląd UI po zastosowaniu WorkoutUiTransformer.kt

fun CircuitWith(name: String, vararg items: WorkoutItem): Circuit {
    return Circuit(
        name = name,
        rounds = 3,
        phase = Phase.WARMUP,
        items = listOf(*items)
    )
}

val EXE = BuiltInExerciseId.BENT_LEG_TWIST.withRepsPerSide(10)

@Preview(locale = "pl")
@Composable
fun WorkoutCardPreviewNested() {//zagnieżdżone wersje
    val workoutDomain = CustomWorkout(
        id = 13.asWorkoutId(),
        name = "nested workout",
        description = "",
        imageUri = null,
        basedOn = BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = listOf(
            //warmup
            CircuitWith(
                "WM1",
                EXE,
                CircuitWith("WM2", EXE)
            ),
            //training
            CircuitWith(
                "TR",
                CircuitWith("SET2", EXE, EXE),
                EXE
            ),
            CircuitWith(
                "finisher",
                CircuitWith(
                    "F2",
                    CircuitWith(
                        "F3",
                        EXE
                    )
                )
            )
        )
    )
    val workoutUiModel = transform(workoutDomain)
    WorkoutCard(
        workout = workoutUiModel,
        onClick = { }
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutCardPreviewAbsWithSet() {//rozgrzewka, trening, wychłodzenie
    val workoutUiModel = transform(MY_ABS_WORKOUT_WITH_SET)
    WorkoutCard(
        workout = workoutUiModel,
        onClick = { }
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutCardPreviewAbsNoSet() {//pojedyńczy poziom, ok 30 ćwiczeń
    val workoutUiModel = transform(MY_WORKOUT_NO_SET)
    WorkoutCard(
        workout = workoutUiModel,
        onClick = { }
    )
}

