package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.common.*
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.compose_multiplatform
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Dopiero na końcu test integracyjny:
 *
 * given flat
 * when drop event
 * then flat result
 *
 * To powinno być cienkie, bo większość logiki już będzie pokryta niżej.
 *
 * Ten test ma sprawdzać integrację warstw, nie detale.
 * TODO - do zrobienia na nowo, na razie dummy test
 */
class WorkoutDropHandlerTest {

    private val handler = WorkoutDropHandlerArchived()

    @Test
    fun `drop on itself should return original workout`() {
        // Given
        val workout = createDummyWorkout(
            items = listOf(
                createDummyExercise(key = 1),
                createDummyExercise(key = 2)
            )
        )
        val event = DragDropEvent(draggedKey = 1, targetKey = 1, position = DropPosition.AFTER)

        // When
        val result = handler.drop(workout, event)

        // Then
        assertEquals(workout, result)
    }

    @Test
    fun `drop with non-existent source key should return original workout`() {
        // Given
        val workout = createDummyWorkout(
            items = listOf(createDummyExercise(key = 1))
        )
        val event = DragDropEvent(draggedKey = 99, targetKey = 1, position = DropPosition.AFTER)

        // When
        val result = handler.drop(workout, event)

        // Then
        assertEquals(workout, result)
    }

    // Helpers to create dummy data for tests
    private fun createDummyWorkout(items: List<WorkoutUiItem>) = WorkoutWithExercisesUiModel(
        workout = WorkoutUiModel(
            workoutId = WorkoutId.Custom(1),
            basedOn = null,
            name = "Test Workout".asUiText(),
            desc = EmptyUiText,
            imageUrl = Res.drawable.compose_multiplatform,
            isInProgress = false,
            difficulty = Difficulty.BEGINNER,
            themeColor = Color.Transparent,
            durationText = "10 min".asUiText(),
            kcalText = "100 kcal".asUiText()
        ),
        items = items
    )

    private fun createDummyExercise(key: Int) = ExerciseUiItem(
        key = key,
        exerciseId = ExerciseId.Custom(1L),
        quantityType = QuantityType.REPS,
        quantityValue = 10,
        name = "Exercise $key".asUiText(),
        icon = Res.drawable.compose_multiplatform
    )
}
