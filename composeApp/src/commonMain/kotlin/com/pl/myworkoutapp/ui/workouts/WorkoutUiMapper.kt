package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.domain.usecase.ExerciseWithMarkdown
import com.pl.myworkoutapp.domain.usecase.WorkoutValidationError
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.exercises.toUi
import com.pl.myworkoutapp.ui.theme.*
import com.pl.myworkoutapp.ui.workouts.details.WorkoutMetadataDraft
import com.pl.myworkoutapp.ui.workouts.tree.toDomain
import com.pl.myworkoutapp.ui.workouts.tree.toTree
import myworkoutapplication.composeapp.generated.resources.*
import kotlin.math.abs

private val CustomThemeColors = listOf(
    StrawberryRed,
    TrafficPurple,
    holoRed,
)

fun Workout.toUi(metrics: WorkoutMetrics): WorkoutUiModel = when (this) {
    is BuiltInWorkout -> {
        val config = id.toBuiltInWorkoutId().toUiConfig()
        WorkoutUiModel(
            workoutId = id,
            basedOn = null,
            difficulty = difficulty,
            name = config.name,
            desc = config.desc,
            image = config.image.asUiImage(),
            isInProgress = false, //TODO
            themeColor = config.color,
            durationText = estimatedDuration.toWorkoutDurationLabel().asUiText(),
            kcalText = metrics.estimatedKcal.toWorkoutKcalText(),
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
            image = when {
                !imageUri.isNullOrEmpty() -> imageUri.asUiImage()
                configBase != null -> configBase.image.asUiImage()
                else -> UiImage.Empty
            },
            isInProgress = false, //TODO
            themeColor = CustomThemeColors[abs(id.hashCode()) % CustomThemeColors.size], //TODO
            durationText = estimatedDuration.toWorkoutDurationLabel().asUiText(),
            kcalText = metrics.estimatedKcal.toWorkoutKcalText(),
        )
    }
}


fun WorkoutExercise.toUiBase(exercise: Exercise): ExerciseUiItem = when (exercise) {
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
            image = config.image.asUiImage(),
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
            image = when {
                !exercise.imageUri.isNullOrEmpty() -> exercise.imageUri.asUiImage()
                configBase != null -> configBase.image.asUiImage()
                else -> UiImage.Empty
            }
        )
    }
}

fun Phase.asUiText(): UiText =
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
        structure = this.structure.toUi(),
        title = if (!name.isNullOrBlank()) name.asUiText() else phase.asUiText(),
    )
}

fun structureStandard(rounds: Int) = CircuitUiStructure(
    type = CircuitStructureType.Standard,
    rounds = rounds
)

fun structureAMRAP(minutes: Int) = CircuitUiStructure(
    type = CircuitStructureType.AMRAP,
    amrapMinutes = minutes
)


fun structureEMOM(minutes: Int) = CircuitUiStructure(
    type = CircuitStructureType.EMOM,
    emomMinutes = minutes
)

fun structureTabata(rounds: Int, workSec: Int, restSec: Int) = CircuitUiStructure(
    type = CircuitStructureType.Tabata,
    rounds = rounds,
    tabataWorkSec = workSec,
    tabataRestSec = restSec,
)

fun CircuitStructure.toUi(): CircuitUiStructure = when (this) {
    is CircuitStructure.Standard -> structureStandard(rounds)

    is CircuitStructure.AMRAP -> structureAMRAP(
        durationSec / 60, //TODO - tu mały problem, wyrównać jednostki
    )

    is CircuitStructure.EMOM -> structureEMOM(minutes)

    is CircuitStructure.Tabata -> structureTabata(
        rounds,
        workSec,
        restSec,
    )
}

fun CircuitStructure.toStructureType() = when (this) {
    is CircuitStructure.Standard -> CircuitStructureType.Standard
    is CircuitStructure.AMRAP -> CircuitStructureType.AMRAP
    is CircuitStructure.EMOM -> CircuitStructureType.EMOM
    is CircuitStructure.Tabata -> CircuitStructureType.Tabata
}

fun CircuitStructureType.asUiText(): UiText = when (this) {
    CircuitStructureType.Standard -> Res.string.circuit_structure_standard.asUiText()
    CircuitStructureType.EMOM -> Res.string.circuit_structure_emom.asUiText()
    CircuitStructureType.AMRAP -> Res.string.circuit_structure_amrap.asUiText()
    CircuitStructureType.Tabata -> Res.string.circuit_structure_tabata.asUiText()
}

fun CircuitUiStructure.getStructureDesc(): UiText = when (this.type) {
    CircuitStructureType.AMRAP -> Res.string.circuit_structure_desc_amrap.asUiText(amrapMinutes)
    CircuitStructureType.EMOM -> Res.string.circuit_structure_desc_emom.asUiText(emomMinutes)
    CircuitStructureType.Standard -> Res.string.circuit_structure_desc_standard.asUiText(rounds)
    CircuitStructureType.Tabata -> Res.string.circuit_structure_desc_tabata.asUiText(
        rounds,
        tabataWorkSec,
        tabataRestSec
    )
}

fun CircuitUiStructure.toDomain(): CircuitStructure = when (type) {
    CircuitStructureType.Standard -> CircuitStructure.Standard(rounds)
    CircuitStructureType.EMOM -> CircuitStructure.EMOM(emomMinutes)
    CircuitStructureType.AMRAP -> CircuitStructure.AMRAP(amrapMinutes * 60) //TODO - ujednolicić jednostki
    CircuitStructureType.Tabata -> CircuitStructure.Tabata(rounds, tabataWorkSec, tabataRestSec)
}

fun ExerciseWithMarkdown.toUi() = exercise.toUi().copy(descriptionMarkdown = descriptionMarkdown)

fun prepareInitialWorkout() = WorkoutUiModel(
    workoutId = WorkoutId.Custom.NEW,
    basedOn = null,
    name = Res.string.workout_initial_name.asUiText(),
    desc = Res.string.workout_initial_desc.asUiText(),
    image = UiImage.Empty,
    isInProgress = false,
    difficulty = Difficulty.INTERMEDIATE,
    themeColor = PearlOpalGreen,
    durationText = EmptyUiText,
    kcalText = EmptyUiText,
)

//uwaga, nie wszystko tutaj zostało przygotowane, nie ma min name i description, które muszą zostać przygotowane w funkcji suspend
fun WorkoutWithExercisesUiModel.toCustomWorkoutForSaving() = CustomWorkout(
    id = when (workout.workoutId) {
        is WorkoutId.BuiltIn -> WorkoutId.Custom.NEW
        is WorkoutId.Custom -> workout.workoutId
    },
    name = "", //workout.name,
    description = "", //workout.desc,
    imageUri = workout.image.localImagePath(),
    basedOn = when (workout.workoutId) {
        is WorkoutId.BuiltIn -> workout.workoutId
        is WorkoutId.Custom -> workout.basedOn
    },
    difficulty = workout.difficulty,
    estimatedDuration = 0,
    baseKcalPerKg = 0.0,
    items = items.toTree().toDomain(),
)


fun WorkoutValidationError.asUiText(): UiText = when (this) {
    WorkoutValidationError.EmptyCircuit -> Res.string.workout_validation_err_empty_circuit.asUiText()
    WorkoutValidationError.EmptyWorkout -> Res.string.workout_validation_err_empty_workout.asUiText()
    WorkoutValidationError.EmptyName -> Res.string.workout_validation_err_empty_name.asUiText()
    WorkoutValidationError.EmptyDescription -> Res.string.workout_validation_err_empty_desc.asUiText()
    WorkoutValidationError.EmptyImage -> Res.string.workout_validation_err_empty_image.asUiText()
}

sealed interface DurationLabel {
    data object Unknown : DurationLabel
    data object Zero : DurationLabel
    data class Seconds(val value: Int) : DurationLabel
    data class MinuteSeconds(val minutes: Int, val seconds: Int) : DurationLabel
    data class Minutes(val value: Int) : DurationLabel
    data class Hours(val value: Int) : DurationLabel
    data class HoursMinutes(val hours: Int, val minutes: Int) : DurationLabel
}

fun Int?.toWorkoutDurationLabel(): DurationLabel {
    return when {
        this == null -> DurationLabel.Unknown
        this < 0 -> DurationLabel.Unknown
        this == 0 -> DurationLabel.Zero
        this < 60 -> DurationLabel.Seconds(((this + 5) / 10) * 10)
        this < 600 -> {
            val rounded = ((this + 5) / 10) * 10
            val minutes = rounded / 60
            val remainder = rounded % 60

            if (remainder == 0) {
                DurationLabel.Minutes(minutes)
            } else {
                DurationLabel.MinuteSeconds(minutes, remainder)
            }
        }

        this < 3600 -> {//powyżej 10 min, poniżej 1 godz
            val minutes = this / 60
            DurationLabel.Minutes(minutes)
        }

        else -> {//powyżej 1h
            var hours = this / 3600
            val remainingMinutes = (this % 3600) / 60
            val roundedMinutes = ((remainingMinutes + 2) / 5) * 5

            if (roundedMinutes == 60) {
                hours += 1
                DurationLabel.Hours(hours)
            } else if (roundedMinutes == 0) {
                DurationLabel.Hours(hours)
            } else {
                DurationLabel.HoursMinutes(hours, roundedMinutes)
            }
        }
    }
}

fun DurationLabel.asUiText(): UiText = when (this) {
    DurationLabel.Unknown -> Res.string.workout_duration_unknown.asUiText()
    DurationLabel.Zero -> Res.string.workout_duration_zero.asUiText()
    is DurationLabel.Hours -> Res.string.workout_duration_hours.asUiText(value)
    is DurationLabel.HoursMinutes -> Res.string.workout_duration_hours.asUiText(hours, minutes)
    is DurationLabel.MinuteSeconds -> Res.string.workout_duration_minute_seconds.asUiText(
        minutes,
        seconds
    )

    is DurationLabel.Minutes -> Res.string.workout_duration_minutes.asUiText(value)
    is DurationLabel.Seconds -> Res.string.workout_duration_seconds.asUiText(value)
}


fun Int?.toWorkoutKcalText(): UiText = when (this) {
    null -> Res.string.workout_kcal_unknown.asUiText()
    else -> Res.string.workout_kcal_pattern.asUiText(this)
}

suspend fun WorkoutWithExercisesUiModel.toMetadataDraft(
    creationMode: Boolean = false,
): WorkoutMetadataDraft {
    return WorkoutMetadataDraft(
        image = workout.image,
        name = workout.name.loadString(),
        description = workout.desc.loadString(),
        creationMode = creationMode,
    )
}