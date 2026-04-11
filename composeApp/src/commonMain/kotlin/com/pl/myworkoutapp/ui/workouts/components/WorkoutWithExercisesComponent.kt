package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseRegistry
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_ABS_WORKOUT_WITH_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_WORKOUT_NO_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.withRepsPerSide
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.theme.FernGreen
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import com.pl.myworkoutapp.ui.theme.StrawberryRed
import com.pl.myworkoutapp.ui.theme.YellowGreen
import com.pl.myworkoutapp.ui.workouts.CircuitUiItem
import com.pl.myworkoutapp.ui.workouts.ExerciseUiItem
import com.pl.myworkoutapp.ui.workouts.TimeLineItemType
import com.pl.myworkoutapp.ui.workouts.WorkoutUiItem
import com.pl.myworkoutapp.ui.workouts.WorkoutUiModel
import com.pl.myworkoutapp.ui.workouts.WorkoutWithExercisesUiModel
import com.pl.myworkoutapp.ui.workouts.transform
import com.pl.myworkoutapp.ui.workouts.with
import kotlinx.coroutines.runBlocking
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch1
import myworkoutapplication.composeapp.generated.resources.ic_jumping_jacks
import myworkoutapplication.composeapp.generated.resources.ic_push_up
import myworkoutapplication.composeapp.generated.resources.ic_rest_day0
import myworkoutapplication.composeapp.generated.resources.ic_rest_day2
import myworkoutapplication.composeapp.generated.resources.ic_side_plank

@Composable
fun WorkoutWithExercisesComponent(
    workoutUiModel: WorkoutWithExercisesUiModel,
    onExerciseClick: (WorkoutUiItem) -> Unit
) {
    val workout = workoutUiModel.workout
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = workout.themeColor)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        WorkoutHeaderCard(
            workout = workout,
            onClick = {}//TODO
        )
        println("WorkoutCardComposable: ${workout.workoutId}")

        Spacer(Modifier.height(16.dp))
        Column(
            //verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            workoutUiModel.items.forEach { item ->
                when(item) {
                    is CircuitUiItem -> WorkoutItemCircuit(
                        item,
                        workout.themeColor,
                        onClick = { onExerciseClick(item) }
                    )
                    is ExerciseUiItem -> WorkoutItemExercise(
                        item,
                        workout.themeColor,
                        onClick = { onExerciseClick(item) }
                    )
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

val PREVIEW_WORKOUT = WorkoutUiModel(
    workoutId = 56789.asWorkoutId(),
    name = "Twój workout ABS".asUiText(),
    desc = "Opis workouta, potrzebny bądź nie".asUiText(),
    imageUrl = Res.drawable.ic_flying_witch1,
    isInProgress = false,
    difficulty = Difficulty.ADVANCED,
    themeColor = PearlOpalGreen,
    durationText = EmptyUiText,
    kcalText = EmptyUiText,
)



val EXE1 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch,
)

val EXE2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch1,
)

val EXE3 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_side_plank,
)

val WARMUP = CircuitUiItem(
    phase = Phase.WARMUP,
    rounds = 2,
    title = "na rozruch".asUiText(),
    progress = 0.47f
)

val TRAINING = CircuitUiItem(
    phase = Phase.MAIN,
    rounds = 3,
    title = "trening właściwy".asUiText(),
    progress = 0.17f
)

val COOLDOWN = CircuitUiItem(
    phase = Phase.COOLDOWN,
    rounds = 3,
    title = "na uspokojenie".asUiText(),
    progress = 0.71f
)

val EXE_WM_1 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "ruszaj się".asUiText(),
    icon = Res.drawable.ic_jumping_jacks,
)
val EXE_WM_2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_push_up,
)

val EXE_CD_1 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_rest_day0,
)
val EXE_CD_2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_rest_day2,
)


@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewBasic() {
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf()
        ),
        onExerciseClick = { }
    )
}

@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewFlat() {
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf(EXE1)
        ),
        onExerciseClick = { }
    )
}

@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewFlatTL() {
    val color1 = PearlOpalGreen
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf(
                EXE1.with(TimeLineItemType.Triple(color1)),
                EXE2.with(TimeLineItemType.Triple(color1)),
                EXE3.with(TimeLineItemType.End(color1)),
            )
        ),
        onExerciseClick = { }
    )
}


@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewCircuit() {
    val color1 = StrawberryRed
    val color2 = FernGreen
    val color3 = YellowGreen
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf(
                WARMUP,
                EXE_WM_1.with(TimeLineItemType.End(color1)),
                WARMUP,
                EXE_WM_1.with(TimeLineItemType.Triple(color1)),
                EXE_WM_2.with(TimeLineItemType.End(color1)),
                TRAINING,
                EXE1.with(TimeLineItemType.Triple(color2)),
                EXE2.with(TimeLineItemType.Triple(color2)),
                EXE3.with(TimeLineItemType.End(color2)),
                COOLDOWN,
                EXE_CD_1.with(TimeLineItemType.Triple(color3)),
                EXE_CD_2.with(TimeLineItemType.End(color3)),
            )
        ),
        onExerciseClick = { }
    )
}



//poniżej testy WorkoutUiTransformer
//w głównej mierze chcemy tutaj przetestować wygląd UI po zastosowaniu WorkoutUiTransformer.kt

fun circuitWith(name: String, vararg items: WorkoutItem): Circuit {
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
fun WorkoutWithExercisesComponentPreviewNested() {//zagnieżdżone wersje
    val workoutDomain = CustomWorkout(
        id = 13.asWorkoutId(),
        name = "nested workout",
        description = "",
        imageUri = null,
        basedOn = BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = listOf(
            //warmup
            circuitWith(
                "WM1",
                EXE,
                circuitWith("WM2", EXE)
            ),
            //training
            circuitWith(
                "TR",
                circuitWith("SET2", EXE, EXE),
                EXE
            ),
            circuitWith(
                "finisher",
                circuitWith(
                    "F2",
                    circuitWith(
                        "F3",
                        EXE
                    )
                )
            )
        )
    )
    //val workoutUiModel = transform(workoutDomain)
    val workoutUiModel = runBlocking {
        transform(workoutDomain) { exerciseId ->
            BuiltInExerciseRegistry.get((exerciseId as ExerciseId.BuiltIn).id)
        }
    }
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onExerciseClick = { }
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutWithExercisesComponentPreviewAbsWithSet() {//rozgrzewka, trening, wychłodzenie
    val workoutUiModel = runBlocking {
        transform(MY_ABS_WORKOUT_WITH_SET) { exerciseId ->
            BuiltInExerciseRegistry.get((exerciseId as ExerciseId.BuiltIn).id)
        }
    }
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onExerciseClick = { }
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutWithExercisesComponentPreviewAbsNoSet() {//pojedyńczy poziom, ok 30 ćwiczeń
    val workoutUiModel = runBlocking {
        transform(MY_WORKOUT_NO_SET) { exerciseId ->
            BuiltInExerciseRegistry.get((exerciseId as ExerciseId.BuiltIn).id)
        }
    }
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onExerciseClick = { }
    )
}

