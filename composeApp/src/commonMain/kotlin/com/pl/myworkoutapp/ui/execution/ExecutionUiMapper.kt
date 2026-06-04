package com.pl.myworkoutapp.ui.execution

import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionUiState.*
import com.pl.myworkoutapp.ui.execution.engine.*
import com.pl.myworkoutapp.ui.workouts.ExerciseUiItem
import com.pl.myworkoutapp.ui.workouts.toUiBase

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
    val current = currentExecutionStepOrNull()
    val currentStepIndex = state.stepIndexOrNull() ?: -1
    val next = nextExerciseOrNull(currentStepIndex + 1)
    val currentExercise = (current as? ExecutionStep.ExerciseStep)
        ?.toUiExercise()
    val nextExercise = next?.toUiExercise()
    val progress = calculateProgress()

    return when (state) {
        is IntroState -> Intro(
            title = "zaczynamy naszą przygodę, powodzenia...".asUiText(),
            nextExercise = nextExercise,
            progress = progress,
            remainingSeconds = state.remainingSeconds,
            canPause = true,
            canSkip = true
        )

        is ExerciseState -> {
            val currentStep = requireNotNull(current as? ExecutionStep.ExerciseStep)
            Exercise(
                title = "exe".asUiText(),//TODO
                currentExercise = requireNotNull(currentExercise),
                nextExercise = nextExercise,
                target = currentStep.toUiTarget(state.targetState),
                progress = progress,
                canPause = true,
                canSkip = true
            )
        }

        is RestState -> Rest(
            title = "Odpoczyszasz..., a mógłbyś ćwiczyć '), dasz radę!!!".asUiText(),//TODO
            progress = progress,
            nextExercise = nextExercise,
            remainingSeconds = state.remainingSeconds,
            canPause = true,
            canSkip = true,
        )

        is FinishedState -> Finished(
            title = "Udało się ukończyć !!!, Gratulacje, spaliłeś XXX kaloriii. Dupa w troki i następnym razem zrób szybciej i dokładniej".asUiText()
        )

        is PausedState -> Paused(
            title = "Nie dajesz rady??? Weź się w garść!!!".asUiText(),//TODO
            currentExercise = currentExercise,
            progress = progress
        )
    }

}

//TODO - na razie tymczasowe rozwiazanie
fun ExecutionStep.ExerciseStep.toUiExercise(): UiExercise = WorkoutExercise(
    exercise.id, quantity
).toUiBase(exercise).let { exeBase: ExerciseUiItem ->
    UiExercise(
        exerciseId = exeBase.exerciseId,
        title = exeBase.name,
        image = exeBase.image,
        quantityLabel = exeBase.quantityValue.toString().asUiText(),//TODO
    )
}

private fun ExecutionStep.ExerciseStep.toUiTarget(
    currentExecution: ExerciseTargetState
): UiExerciseTarget {
    return when (quantity.type) {
        QuantityType.DURATION -> {
            UiExerciseTarget.Duration(
                remainingSeconds =
                    (currentExecution as? ExerciseTargetState.Countdown)
                        ?.remainingSeconds
                        ?: 0
            )
        }

        QuantityType.REPS,
        QuantityType.REPS_PER_SIDE -> {
            UiExerciseTarget.Reps(
                reps = quantity.value
            )
        }
        QuantityType.DISTANCE -> {
            UiExerciseTarget.Distance(
                meters = quantity.value
            )
        }
    }
}
