package com.pl.myworkoutapp.ui.workouts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.common.*
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class WorkoutUiModel(
    val workoutId: WorkoutId,
    val basedOn: WorkoutId.BuiltIn?,
    val name: UiText,
    val desc: UiText,
    val imageUrl: DrawableResource,
    val isInProgress: Boolean,
    val difficulty: Difficulty,
    val themeColor: Color,
    val durationText: UiText,
    val kcalText: UiText,
)

@Immutable
data class WorkoutWithExercisesUiModel(
    val workout: WorkoutUiModel,
    val items: List<WorkoutUiItem>,
) {
    fun isDirty(original: WorkoutWithExercisesUiModel): Boolean {
        return !(workout == original.workout && items == original.items)
    }
}

@Immutable
sealed interface TimeLineItemType {
    val color: Color

    data class None(override val color: Color = Color.Transparent) :
        TimeLineItemType //gdy będzie pojedyńczy element

    data class End(override val color: Color) : TimeLineItemType
    data class Vertical(override val color: Color) : TimeLineItemType //tylko linia pionowa
    data class Triple(override val color: Color) :
        TimeLineItemType //linia pionowa ze wskaźnikiem(w prawo)
}

//tutaj będzie odpowiednik albo WorkoutExercise albo Circuit
@Immutable
sealed interface WorkoutUiItem {
    val isCurrent: Boolean
    val isDone: Boolean
    val timeline: List<TimeLineItemType>
    val key: Int //unikalny id obiektu będącego na liście w workout(klucz dla operacji UI)
    val depth: Int //głębokość w strukturze/drzewie
}

@Immutable
data class ExerciseUiItem(
    override val isCurrent: Boolean = false,
    override val isDone: Boolean = false,
    override val timeline: List<TimeLineItemType> = listOf(),
    override val key: Int = 0,
    override val depth: Int = 0,

    val exerciseId: ExerciseId,
    //val muscle: MuscleGroup,
    val quantityType: QuantityType,
    val quantityValue: Int,
    val name: UiText,
    val icon: DrawableResource //TODO - to powinna być albo ikona albo ścieżka do obrazka
) : WorkoutUiItem

enum class CircuitStructureType {
    Standard, EMOM, AMRAP, Tabata
}

data class CircuitUiStructure(
    val type: CircuitStructureType,
    val rounds: Int = 0,
    val emomMinutes: Int = 0,
    val amrapMinutes: Int = 0,
    val tabataRounds: Int = 0,
    val tabataWorkSec: Int = 0,
    val tabataRestSec: Int = 0,
)

@Immutable
data class CircuitUiItem(
    override val isCurrent: Boolean = false,
    override val isDone: Boolean = false,
    override val timeline: List<TimeLineItemType> = listOf(),
    override val key: Int = 0,
    override val depth: Int = 0,

    val phase: Phase,
    val structure: CircuitUiStructure = structureStandard(2),
    val title: UiText,
    val progress: Float? = null,
) : WorkoutUiItem

//fun WorkoutUiModel.with(vararg items: WorkoutUiItem) = WorkoutWithExercisesUiModel(
//    workout = this,
//    items = listOf(*items)
//)
fun ExerciseUiItem.with(vararg ts: TimeLineItemType) = copy(timeline = listOf(*ts))
fun CircuitUiItem.with(vararg ts: TimeLineItemType) = copy(timeline = listOf(*ts))

