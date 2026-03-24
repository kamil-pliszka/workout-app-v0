package com.pl.myworkoutapp.ui.workouts

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.common.UiText
import org.jetbrains.compose.resources.DrawableResource

data class WorkoutUiModel(
    val workoutId: WorkoutId,
    val name: UiText,
    val desc: UiText,
    val imageUrl: DrawableResource,
    val items: List<WorkoutUiItem>,
    val isInProgress: Boolean,
    val difficulty: Difficulty,
    val themeColor: Color,
)

sealed interface TimeLineItemType {
    val color: Color
    data class None(override val color: Color = Color.Transparent): TimeLineItemType //gdy będzie pojedyńczy element
    data class End(override val color: Color): TimeLineItemType
    data class Vertical(override val color: Color): TimeLineItemType //tylko linia pionowa
    data class Triple(override val color: Color): TimeLineItemType //linia pionowa ze wskaźnikiem(w prawo)
}

//tutaj będzie odpowiednik albo WorkoutExercise albo Circuit
sealed interface WorkoutUiItem {
    val isCurrent: Boolean
    val isDone: Boolean
    val timeline: List<TimeLineItemType>
}

data class ExerciseUiItem(
    override val isCurrent: Boolean = false,
    override val isDone: Boolean = false,
    override val timeline: List<TimeLineItemType> = listOf(),

    val exerciseId: ExerciseId,
    //val muscle: MuscleGroup,
    //val quantityType: QuantityType,
    //val quantityValue: Int,
    val quantityText: UiText,
    val name: UiText,
    //val desc: UiText,
    val icon: DrawableResource
) : WorkoutUiItem

data class CircuitUiItem(
    override val isCurrent: Boolean = false,
    override val isDone: Boolean = false,
    override val timeline: List<TimeLineItemType> = listOf(),

    val phase: Phase,
    val rounds: Int,
    //trochę przenika, ale nie chce mi się robić kopii, bo jeszcze nie wiem jak to wykorzystam
    val structure: CircuitStructure = CircuitStructure.Standard,
    val title: UiText,
    val progress: Float? = null,
    //val subtitle: UiText?,
) : WorkoutUiItem

fun WorkoutUiModel.with(vararg items: WorkoutUiItem) = copy(items = listOf(*items))
fun ExerciseUiItem.with(vararg ts: TimeLineItemType) = copy(timeline = listOf(*ts))
fun CircuitUiItem.with(vararg ts: TimeLineItemType) = copy(timeline = listOf(*ts))
