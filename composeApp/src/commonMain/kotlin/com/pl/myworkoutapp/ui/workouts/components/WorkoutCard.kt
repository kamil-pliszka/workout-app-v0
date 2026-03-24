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
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.asWorkoutId
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
import com.pl.myworkoutapp.ui.workouts.with
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch1
import myworkoutapplication.composeapp.generated.resources.ic_jumping_jacks
import myworkoutapplication.composeapp.generated.resources.ic_push_up
import myworkoutapplication.composeapp.generated.resources.ic_rest_day0
import myworkoutapplication.composeapp.generated.resources.ic_rest_day2
import myworkoutapplication.composeapp.generated.resources.ic_side_plank
import myworkoutapplication.composeapp.generated.resources.test_string_2_param

@Composable
fun WorkoutCard(
    workout: WorkoutUiModel,
    onClick: (WorkoutUiItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = workout.themeColor)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        WorkoutHeader(workout = workout)
        println("WorkoutCardComposable: ${workout.workoutId}")

        Spacer(Modifier.height(16.dp))
        Column(
            //verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            workout.items.forEach { item ->
                when(item) {
                    is CircuitUiItem -> WorkoutItemCircuit(
                        item,
                        workout.themeColor,
                        onClick = { onClick(item) }//TODO - tu powinien być podgląd ćwiczenia
                    )
                    is ExerciseUiItem -> WorkoutItemExercise(
                        item,
                        workout.themeColor,
                        onClick = { onClick(item) }//TODO - tu powinien być podgląd ćwiczenia
                    )
                }
            }
        }
    }
}

val PREVIEW_WORKOUT = WorkoutUiModel(
    workoutId = 56789.asWorkoutId(),
    name = "Twój workout ABS".asUiText(),
    desc = "Opis workouta, potrzebny bądź nie".asUiText(),
    imageUrl = Res.drawable.ic_flying_witch1,
    items = listOf(),
    isInProgress = false,
    difficulty = Difficulty.ADVANCED,
    themeColor = PearlOpalGreen
)



val EXE1 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = "do świtu".asUiText(),
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch,
)

val EXE2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = "do świtu".asUiText(),
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch1,
)

val EXE3 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = Res.string.test_string_2_param.asUiText("rano", "świt"),
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
    quantityText = "3 godz i trochę".asUiText(),
    name = "ruszaj się".asUiText(),
    icon = Res.drawable.ic_jumping_jacks,
)
val EXE_WM_2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = "przez chwilę".asUiText(),
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_push_up,
)

val EXE_CD_1 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = "przez chwilę".asUiText(),
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_rest_day0,
)
val EXE_CD_2 = ExerciseUiItem(
    exerciseId = 123.asExerciseId(),
    quantityText = "przez chwilę".asUiText(),
    name = "fiku miku".asUiText(),
    icon = Res.drawable.ic_rest_day2,
)


@Preview
@Composable
fun WorkoutCardPreviewBasic() {
    WorkoutCard(
        workout = PREVIEW_WORKOUT,
        onClick = { }
    )
}

@Preview
@Composable
fun WorkoutCardPreviewFlat() {
    WorkoutCard(
        workout = PREVIEW_WORKOUT.with(
            EXE1,
        ),
        onClick = { }
    )
}

@Preview
@Composable
fun WorkoutCardPreviewFlatTL() {
    val color1 = PearlOpalGreen
    WorkoutCard(
        workout = PREVIEW_WORKOUT.with(
            EXE1.with(TimeLineItemType.Triple(color1)),
            EXE2.with(TimeLineItemType.Triple(color1)),
            EXE3.with(TimeLineItemType.End(color1)),
        ),
        onClick = { }
    )
}


@Preview
@Composable
fun WorkoutCardPreviewCircuit() {
    val color1 = StrawberryRed
    val color2 = FernGreen
    val color3 = YellowGreen
    WorkoutCard(
        workout = PREVIEW_WORKOUT.with(
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
        ),
        onClick = { }
    )
}


