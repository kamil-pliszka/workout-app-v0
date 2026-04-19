package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.theme.*
import myworkoutapplication.composeapp.generated.resources.*

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
            basedOn = null,
            difficulty = difficulty,
            name = config.name,
            desc = config.desc,
            imageUrl = config.image,
            isInProgress = false, //TODO
            themeColor = config.color,
            durationText = "17 min".asUiText(),//TODO
            kcalText = "≈317 kCal".asUiText(),//TODO
        )
    }
    is CustomWorkout -> {
        val configBase = basedOn?.toBuiltInWorkoutId()?.toUiConfig()
        return WorkoutUiModel(
            workoutId = id,
            basedOn = basedOn,
            difficulty = difficulty,
            name = when {
                !name.isNullOrBlank() -> name.asUiText()
                configBase != null -> configBase.name
                else -> EmptyUiText
            },
            desc = when {
                !description.isNullOrBlank() -> description.asUiText()
                configBase != null -> configBase.desc
                else -> EmptyUiText
            },
            imageUrl = when {
                !imageUri.isNullOrEmpty() -> Res.drawable.ic_flying_witch1 //TODO
                configBase != null -> configBase.image
                else -> Res.drawable.compose_multiplatform //TODO
            },
            isInProgress = false, //TODO
            themeColor = CustomThemeColors[id.hashCode() % CustomThemeColors.size], //TODO
            durationText = "13 min".asUiText(),//TODO
            kcalText = "≈123 kCal".asUiText(),//TODO
        )
    }
}




fun WorkoutExercise.toUiBase(exercise: Exercise): ExerciseUiItem = when(exercise) {
    is BuiltInExercise -> {
        val config = exercise.id.toBuiltInExerciseId().toUiConfig()
        ExerciseUiItem(
            isCurrent = false, //na tym poziomie nie mamy wiedzy
            isDone = false, //na tym poziomie nie mamy wiedzy
            timeline = emptyList(), //na tym poziomie nie mamy wiedzy
            exerciseId = exercise.id,
            quantityType = this.quantity.type,
            quantityValue = this.quantity.value,
            name = config.name.asUiText(),
            icon = config.image,
        )
    }
    is CustomExercise -> {
        val configBase = exercise.basedOn?.toBuiltInExerciseId()?.toUiConfig()
        ExerciseUiItem(
            isCurrent = false, //na tym poziomie nie mamy wiedzy
            isDone = false, //na tym poziomie nie mamy wiedzy
            timeline = emptyList(), //na tym poziomie nie mamy wiedzy
            exerciseId = exercise.id,
            quantityType = this.quantity.type,
            quantityValue = this.quantity.value,
            name = when {
                exercise.name.isNotBlank() -> exercise.name.asUiText()
                configBase != null -> configBase.name.asUiText()
                else -> EmptyUiText
            },
            icon = when {
                !exercise.imageUri.isNullOrEmpty() -> Res.drawable.ic_flying_witch1 //TODO
                configBase != null -> configBase.image
                else -> Res.drawable.compose_multiplatform //TODO
            }
        )
    }
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

fun CircuitStructure.toStructureType() = when(this) {
    CircuitStructure.Standard -> CircuitStructureType.Standard
    is CircuitStructure.AMRAP -> CircuitStructureType.AMRAP
    is CircuitStructure.EMOM -> CircuitStructureType.EMOM
    is CircuitStructure.Tabata -> CircuitStructureType.Tabata
}

fun CircuitStructureType.asUiText() : UiText = when(this) {
    CircuitStructureType.Standard -> Res.string.circuit_structure_standard.asUiText()
    CircuitStructureType.EMOM -> Res.string.circuit_structure_emom.asUiText()
    CircuitStructureType.AMRAP -> Res.string.circuit_structure_amrap.asUiText()
    CircuitStructureType.Tabata -> Res.string.circuit_structure_tabata.asUiText()
}