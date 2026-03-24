package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.Quantity
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkout
import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.CustomWorkout
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise
import com.pl.myworkoutapp.domain.model.workout.toBuiltInWorkoutId
import com.pl.myworkoutapp.domain.model.workout.toInt
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.common.toUiConfig
import com.pl.myworkoutapp.ui.theme.StrawberryRed
import com.pl.myworkoutapp.ui.theme.TrafficPurple
import com.pl.myworkoutapp.ui.theme.holoRed
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.compose_multiplatform
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch1
import myworkoutapplication.composeapp.generated.resources.ic_rest_day1
import myworkoutapplication.composeapp.generated.resources.workouts_phase_coldown
import myworkoutapplication.composeapp.generated.resources.workouts_phase_training
import myworkoutapplication.composeapp.generated.resources.workouts_phase_warmup
import myworkoutapplication.composeapp.generated.resources.workouts_qty_reps
import myworkoutapplication.composeapp.generated.resources.workouts_qty_reps_per_side

private val CustomThemeColors = listOf(
    StrawberryRed,
    TrafficPurple,
    holoRed,
)

fun Workout.toUi(): WorkoutUiModel = when(this) {
    is BuiltInWorkout -> {
        val config = id.toBuiltInWorkoutId().toUiConfig()
        WorkoutUiModel(
            workoutId = id,
            difficulty = difficulty,
            name = config.name,
            desc = config.desc,
            imageUrl = config.image,
            items = emptyList(), //tutaj nie mapujemy ćwiczeń
            isInProgress = false, //TODO
            themeColor = config.color
        )
    }
    is CustomWorkout -> {
        val config = basedOn?.toBuiltInWorkoutId()?.toUiConfig()
        return WorkoutUiModel(
            workoutId = id,
            difficulty = difficulty,
            name = when {
                name.isNotBlank() -> name.asUiText()
                config != null -> config.name
                else -> EmptyUiText
            },
            desc = when {
                !description.isNullOrBlank() -> description.asUiText()
                config != null -> config.desc
                else -> EmptyUiText
            },
            imageUrl = when {
                !imageUri.isNullOrEmpty() -> Res.drawable.ic_flying_witch1 //TODO
                config != null -> config.image
                else -> Res.drawable.compose_multiplatform //TODO
            },
            items = emptyList(), // items są budowane w WorkoutUiTransformer (flatten + timeline)
            isInProgress = false, //TODO
            themeColor = CustomThemeColors[id.toInt() % CustomThemeColors.size]
        )
    }
}

private fun Int.pad2(): String = this.toString().padStart(2, '0')
private fun Int.pad3(): String = this.toString().padStart(3, '0')

fun Int.secondsAsText(): String {
    return when {
        this < 0 -> "?"
        this == 0 -> "0"
        this < 60 -> this.toString() + "s"
        else -> {
            val minutes = this / 60
            val seconds = this % 60
            "${minutes.pad2()}:${seconds.pad2()}"
        }
    }
}

fun Int.distanceAsText(): String {
    return when {
        this < 0 -> "?"
        this == 0 -> "0"
        this < 1000 -> this.toString() + "m"
        else -> {
            val km = this / 1000
            val m = this % 1000
            "${km}.${m.pad3()} km"
        }
    }
}

fun Quantity.asUiText(): UiText {
    return when (type) {
        QuantityType.REPS -> Res.string.workouts_qty_reps.asUiText(value)
        QuantityType.REPS_PER_SIDE -> Res.string.workouts_qty_reps_per_side.asUiText(value)
        QuantityType.DURATION -> value.secondsAsText().asUiText()
        QuantityType.DISTANCE -> value.distanceAsText().asUiText()
    }
}

fun WorkoutExercise.toUiBase(): ExerciseUiItem {
    return ExerciseUiItem(
        isCurrent = false, //na tym poziomie nie mamy wiedzy
        isDone = false, //na tym poziomie nie mamy wiedzy
        timeline = emptyList(), //na tym poziomie nie mamy wiedzy
        exerciseId = this.exercise.id,
        quantityText = this.quantity.asUiText(),
        name = this.exercise.id.toString().asUiText(), //TODO - item.exercise.name,
        icon = Res.drawable.ic_rest_day1 //TODO item.exercise.icon
    )
}

fun Phase.asUiText() : UiText =
    when (this) {
        Phase.WARMUP -> Res.string.workouts_phase_warmup.asUiText()
        Phase.COOLDOWN -> Res.string.workouts_phase_coldown.asUiText()
        Phase.MAIN -> Res.string.workouts_phase_training.asUiText()
    }


fun Circuit.toUiBase(): CircuitUiItem {
    return CircuitUiItem(
        isCurrent = false, //na tym poziomie nie mamy wiedzy
        isDone = false, //na tym poziomie nie mamy wiedzy
        timeline = emptyList(), //na tym poziomie nie mamy wiedzy
        progress = null, //na tym poziomie nie mamy wiedzy
        phase = this.phase,
        rounds = this.rounds,
        structure = this.structure,
        title = if (!name.isNullOrBlank()) name.asUiText() else phase.asUiText(),
    )
}
