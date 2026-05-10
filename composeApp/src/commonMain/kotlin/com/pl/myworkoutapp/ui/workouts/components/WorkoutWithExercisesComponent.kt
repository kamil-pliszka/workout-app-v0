package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_ABS_WORKOUT_WITH_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.AbsWorkouts.MY_WORKOUT_NO_SET
import com.pl.myworkoutapp.domain.model.workout.builtin.withRepsPerSide
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.asUiImage
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.CircleIconButton
import com.pl.myworkoutapp.ui.theme.*
import com.pl.myworkoutapp.ui.workouts.*
import com.pl.myworkoutapp.ui.workouts.tree.transform
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


sealed interface WorkoutWithExercisesAction {
    object OnBack : WorkoutWithExercisesAction
    object OnOpenEditor : WorkoutWithExercisesAction
    object OnDeleteRequest : WorkoutWithExercisesAction
    object OnResetRequest : WorkoutWithExercisesAction
    object OnTuneRequest : WorkoutWithExercisesAction
}

@Composable
fun WorkoutWithExercisesComponent(
    workoutUiModel: WorkoutWithExercisesUiModel,
    onAction: (WorkoutWithExercisesAction) -> Unit,
    onShowExerciseInfo: (key: Int, exerciseId: ExerciseId) -> Unit,
) {
    val workout = workoutUiModel.workout
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = workout.themeColor)
            .padding(horizontal = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            WorkoutHeaderCard(
                workout = workout,
                onClick = { }, //ale właścwie co robić?
                //onNameChanged = { onAction(WorkoutWithExercisesAction.OnNameChanged(it)) },
                //onDescChanged = { onAction(WorkoutWithExercisesAction.OnDescChanged(it)) },
            )
            WorkoutHeaderActionsBox(
                workout = workoutUiModel.workout,
                onBack = { onAction(WorkoutWithExercisesAction.OnBack) },
                onEdit = { onAction(WorkoutWithExercisesAction.OnOpenEditor) },
                onDelete = { onAction(WorkoutWithExercisesAction.OnDeleteRequest) },
                onReset = { onAction(WorkoutWithExercisesAction.OnResetRequest) },
                onTune = { onAction(WorkoutWithExercisesAction.OnTuneRequest) },
            )
        }
        Spacer(Modifier.height(16.dp))

        //verticalArrangement = Arrangement.spacedBy(12.dp)
        Column {
            if (workoutUiModel.items.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(Res.string.workout_empty_items),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            workoutUiModel.items.forEach { item ->
                WorkoutItemRow(
                    item = item,
                    themeColor = workout.themeColor,
                    onShowExerciseInfo = onShowExerciseInfo
                )
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun WorkoutItemRow(
    item: WorkoutUiItem,
    themeColor: Color,
    onShowExerciseInfo: (key: Int, exerciseId: ExerciseId) -> Unit,
) {
    val desc = when (item) {
        is CircuitUiItem -> "${item.title}"
        is ExerciseUiItem -> "${item.exerciseId}"
    }
    println("WorkoutItemRow: ${item.key}, $desc")
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (item) {
            is CircuitUiItem -> WorkoutItemCircuit(
                item,
                themeColor,
                onClick = { /*onAction(WorkoutWithExercisesAction.ShowExerciseInfo(item))*/ }
            )

            is ExerciseUiItem -> WorkoutItemExercise(
                item,
                themeColor,
                onClick = {
                    onShowExerciseInfo(
                        //WorkoutWithExercisesAction.ShowExerciseInfo(
                            item.key,
                            item.exerciseId
                        //)
                    )
                }
            )
        }
    }
}

@Composable
fun BoxScope.WorkoutHeaderActionsBox(
    workout: WorkoutUiModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReset: () -> Unit,
    onTune: () -> Unit,
) {
    // LEWA IKONA (back)
    CircleIconButton(
        icon = Res.drawable.ic_arrow_back,
        modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp),
        onClick = onBack
    )
    // PRAWA GRUPA IKON
    Row(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        CircleIconButton(
            icon = Res.drawable.ic_edit_square,
            onClick = onEdit
        )

        if (workout.workoutId is WorkoutId.Custom && !workout.workoutId.isNew()) {
            CircleIconButton(
                icon = if (workout.basedOn != null) Res.drawable.ic_reset_settings else Res.drawable.ic_delete,
                onClick = if (workout.basedOn != null) onReset else onDelete
            )
        }
        CircleIconButton(
            icon = Res.drawable.ic_tune,
            onClick = onTune
        )
    }
}

val PREVIEW_WORKOUT = WorkoutUiModel(
    workoutId = WorkoutId.Custom.NEW,
    basedOn = null,
    name = "Twój workout ABS".asUiText(),
    desc = "Opis workouta, potrzebny bądź nie".asUiText(),
    image = Res.drawable.ic_flying_witch1.asUiImage(),
    isInProgress = false,
    difficulty = Difficulty.ADVANCED,
    themeColor = PearlOpalGreen,
    durationText = EmptyUiText,
    kcalText = EmptyUiText,
)


val EXE1 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    image = Res.drawable.ic_flying_witch.asUiImage(),
)

val EXE2 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    image = Res.drawable.ic_flying_witch1.asUiImage(),
)

val EXE3 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    image = Res.drawable.ic_side_plank.asUiImage(),
)

val WARMUP = CircuitUiItem(
    phase = Phase.WARMUP,
    structure = structureStandard(2),
    title = "na rozruch".asUiText(),
    progress = 0.47f
)

val TRAINING = CircuitUiItem(
    phase = Phase.MAIN,
    structure = structureStandard(3),
    title = "trening właściwy".asUiText(),
    progress = 0.17f
)

val COOLDOWN = CircuitUiItem(
    phase = Phase.COOLDOWN,
    structure = structureStandard(3),
    title = "na uspokojenie".asUiText(),
    progress = 0.71f
)

val EXE_WM_1 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "ruszaj się".asUiText(),
    image = Res.drawable.ic_jumping_jacks.asUiImage(),
)
val EXE_WM_2 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    image = Res.drawable.ic_push_up.asUiImage(),
)

val EXE_CD_1 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    image = Res.drawable.ic_rest_day0.asUiImage(),
)
val EXE_CD_2 = ExerciseUiItem(
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "fiku miku".asUiText(),
    image = Res.drawable.ic_rest_day2.asUiImage(),
)

fun List<WorkoutUiItem>.prepareKeys() = this.mapIndexed { index, item ->
    when (item) {
        is CircuitUiItem -> item.copy(key = index)
        is ExerciseUiItem -> item.copy(key = index)
    }
}

@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewBasic() {
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf()
        ),
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
    )
}

@Preview
@Composable
fun WorkoutWithExercisesComponentPreviewFlat() {
    WorkoutWithExercisesComponent(
        workoutUiModel = WorkoutWithExercisesUiModel(
            workout = PREVIEW_WORKOUT,
            items = listOf(EXE1),
        ),
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
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
            ).prepareKeys()
        ),
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
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
            ).prepareKeys()
        ),
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
    )
}


//poniżej testy WorkoutUiTransformer
//w głównej mierze chcemy tutaj przetestować wygląd UI po zastosowaniu WorkoutTreeDomainMapper.kt

fun circuitWith(name: String, vararg items: WorkoutItem): Circuit {
    return Circuit(
        name = name,
        structure = CircuitStructure.Standard(3),
        phase = Phase.WARMUP,
        items = listOf(*items)
    )
}

val EXE = BuiltInExerciseId.BENT_LEG_TWIST.withRepsPerSide(10)

@Preview(locale = "pl")
@Composable
fun WorkoutWithExercisesComponentPreviewNested() {//zagnieżdżone wersje
    val workoutDomain = CustomWorkout(
        id = 13L.asWorkoutId(),
        name = "nested workout",
        description = "",
        imageUri = null,
        basedOn = BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        estimatedDuration = 13,
        baseKcalPerKg = 567.0,
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
    val workoutUiModel = transform(workoutDomain, WorkoutMetrics(567))
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutWithExercisesComponentPreviewAbsWithSet() {//rozgrzewka, trening, wychłodzenie
    val workoutUiModel = transform(MY_ABS_WORKOUT_WITH_SET, WorkoutMetrics(567))
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
    )
}


@Preview(locale = "pl")
@Composable
fun WorkoutWithExercisesComponentPreviewAbsNoSet() {//pojedyńczy poziom, ok 30 ćwiczeń
    val workoutUiModel = transform(MY_WORKOUT_NO_SET, WorkoutMetrics(567))
    WorkoutWithExercisesComponent(
        workoutUiModel = workoutUiModel,
        onAction = { },
        onShowExerciseInfo = { _, _ -> },
    )
}

