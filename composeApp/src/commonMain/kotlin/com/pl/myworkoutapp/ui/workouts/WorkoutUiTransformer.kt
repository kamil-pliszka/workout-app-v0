package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.workout.Circuit
import com.pl.myworkoutapp.domain.model.workout.Workout
import com.pl.myworkoutapp.domain.model.workout.WorkoutExercise
import com.pl.myworkoutapp.domain.model.workout.WorkoutItem
import com.pl.myworkoutapp.ui.theme.BrillantBlue
import com.pl.myworkoutapp.ui.theme.FernGreen
import com.pl.myworkoutapp.ui.theme.PearlOpalGreen
import com.pl.myworkoutapp.ui.theme.TrafficPurple


data class WorkoutTraversalItem(
    val item: WorkoutItem,
    val level: Int,
    val isLast: Boolean,
    //val parent: WorkoutTraversalItem?
    val ancestors: List<Boolean> // czy na danym poziomie są jeszcze elementy poniżej
)

fun List<WorkoutItem>.flatten(): List<WorkoutTraversalItem> {
    val result = mutableListOf<WorkoutTraversalItem>()

    fun visit(
        item: WorkoutItem,
        level: Int,
        isLast: Boolean,
        ancestors: List<Boolean>
    ) {
        result += WorkoutTraversalItem(item, level, isLast, ancestors)

        if (item is Circuit) {
            item.items.forEachIndexed { index, child ->
                val isChildLast = index == item.items.lastIndex
                val childAncestors = ancestors + isLast
                visit(child, level + 1, isChildLast, childAncestors)
            }
        }
    }

    forEachIndexed { index, item ->
        val isLast = index == lastIndex
        visit(item, level = 0, isLast = isLast, ancestors = emptyList())
    }

    return result
}

//na razie bardzo prosto
val treeColors = listOf(
    TrafficPurple,
    PearlOpalGreen,
    FernGreen,
    BrillantBlue,
)

//TODO - tu jeszcze powinno się uwzględnić odpowiedni kolor, może na razie wystarczy na podstawie level
fun WorkoutTraversalItem.toTimeline(): List<TimeLineItemType> {
    val result = mutableListOf<TimeLineItemType>()
    ancestors.forEachIndexed { levelIndex, ancestor ->
        val color = treeColors[levelIndex % treeColors.size]
        result.add(if (ancestor) TimeLineItemType.None() else TimeLineItemType.Vertical(color))
    }
    val color = treeColors[level % treeColors.size]
    result.add(if (isLast) TimeLineItemType.End(color) else TimeLineItemType.Triple(color))
    return result
}


fun transform(workout: Workout): WorkoutUiModel {
    val workoutUiModel = workout.toUi()
    val traversalItems = workout.items.flatten()

    val itemsUiModel =  traversalItems.map { workoutTraversalItem ->
        when (workoutTraversalItem.item) {
            is Circuit -> workoutTraversalItem.item.toUiBase().copy(
                timeline = workoutTraversalItem.toTimeline()
            )
            is WorkoutExercise -> workoutTraversalItem.item.toUiBase().copy(
                timeline = workoutTraversalItem.toTimeline()
            )
        }
    }
    return workoutUiModel.copy(
        items = itemsUiModel
    )
}
