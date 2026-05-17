package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.ui.execution.engine.*

/**
 * Adapter:
 * WorkoutExecutionRuntime -> WorkoutExecutionUiState
 *
 * Odpowiada za:
 * transformację runtime na dane dla Compose
 * wybór current/next exercise
 * progress
 * mapowanie phase -> UiPhase
 *
 * Nie powinien:
 *
 * zmieniać runtime
 * wykonywać logiki workflow
 */


fun WorkoutExecutionRuntime.toUiState(): WorkoutExecutionUiState {
    val current = currentStep
    val next = steps.drop(currentStepIndex + 1)
        .filterIsInstance<ExecutionStep.ExerciseStep>()
        .firstOrNull()
    val currentExercise = (current as? ExecutionStep.ExerciseStep)
        ?.toUiExercise()
    val nextExercise = next?.toUiExercise()
    val progress = (currentStepIndex + 1).toFloat() / steps.size

    return when(phase) {
        ExecutionPhase.Intro -> WorkoutExecutionUiState.Intro(
            title = "zaczynamy naszą przygodę, powodzenia...",
            nextExercise = nextExercise,
            progress = progress,
            remainingSeconds = remainingSeconds ?: 0,
            canPause = true,
            canSkip = true
        )
        ExecutionPhase.Exercise -> {
            val currentExercise =
                requireNotNull(currentExercise)
            WorkoutExecutionUiState.Exercise(
                title = "exe",//TODO
                currentExercise = currentExercise,
                nextExercise = nextExercise,
                remainingSeconds = remainingSeconds,
                progress = progress,
                canPause = true,
                canSkip = true
            )
        }
        ExecutionPhase.Paused -> WorkoutExecutionUiState.Paused(
            title = "Nie dajesz rady??? Weź się w garść!!!",//TODO
            currentExercise = currentExercise,
            progress = progress
        )
        ExecutionPhase.Rest -> WorkoutExecutionUiState.Rest(
            title = "Odpoczyszasz..., a mógłbyś ćwiczyć '), dasz radę!!!",//TODO
            progress = progress,
            nextExercise = nextExercise,
            remainingSeconds = remainingSeconds ?: 0,
            canPause = true,
            canSkip = true,
        )
        ExecutionPhase.Finished -> WorkoutExecutionUiState.Finished(
            title = "Udało się ukończyć !!!, Gratulacje, spaliłeś XXX kaloriii. Dupa w troki i następnym razem zrób szybciej i dokładniej"
        )

    }

}

fun ExecutionStep.ExerciseStep.toUiExercise() = UiExercise(
    exerciseId = this.exercise.id
    //TODO - reszta
)

fun ExecutionStep.ExerciseStep.countdownDurationSeconds() : Int? = when(quantity.type) {
    QuantityType.DURATION -> quantity.value
    else -> null
}
