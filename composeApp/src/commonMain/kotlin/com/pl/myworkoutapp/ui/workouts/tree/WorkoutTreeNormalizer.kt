package com.pl.myworkoutapp.ui.workouts.tree

import com.pl.myworkoutapp.ui.theme.*
import com.pl.myworkoutapp.ui.workouts.*

private data class TreeTraversalItem(
    val item: WorkoutUiItem,
    val depth: Int,
    val ancestorsTL: List<Boolean> // czy na danym poziomie są jeszcze elementy poniżej
)

class WorkoutTreeNormalizer {
    fun normalize(
        workout: WorkoutWithExercisesUiModel,
        mutated: List<TreeNode>
    ): WorkoutWithExercisesUiModel {
        return workout.copy(
            //trzeba zrobić normalizację
            //Musisz przeliczyć:
            //depth, timeline
            items = mutated.normalizeToUi()
        )
    }
}

private fun List<TreeNode>.flatten(): List<TreeTraversalItem> {
    val result = mutableListOf<TreeTraversalItem>()

    fun visit(
        item: TreeNode,
        depth: Int,
        isLast: Boolean,
        ancestors: List<Boolean>
    ) {
        val uiItem = when (item) {
            is CircuitNode -> item.circuit
            is ExerciseNode -> item.exercise
        }
        result += TreeTraversalItem(uiItem, depth, ancestors + isLast)

        if (item is CircuitNode) {
            item.children.forEachIndexed { index, child ->
                val isChildLast = index == item.children.lastIndex
                val childAncestors = ancestors + isLast
                visit(child, depth + 1, isChildLast, childAncestors)
            }
        }
    }

    forEachIndexed { index, item ->
        val isLast = index == lastIndex
        visit(item, depth = 0, isLast = isLast, ancestors = emptyList())
    }
    return result
}

fun List<TreeNode>.normalizeToUi(): List<WorkoutUiItem> {
    val traversalItems = flatten()
    return traversalItems.map { flat ->
        when (flat.item) {
            is CircuitUiItem -> flat.item.copy(
                depth = flat.depth,
                timeline = flat.ancestorsTL.toTimeline()
            )

            is ExerciseUiItem -> flat.item.copy(
                depth = flat.depth,
                timeline = flat.ancestorsTL.toTimeline()
            )
        }
    }
}

//na razie bardzo prosto
val treeColors = listOf(
    TrafficPurple,
    PearlOpalGreen,
    FernGreen,
    BrillantBlue,
)

fun List<Boolean>.toTimeline(): List<TimeLineItemType> {
    return this.mapIndexed { levelIndex, isLast ->
        val color = treeColors[levelIndex % treeColors.size]
        if (levelIndex == lastIndex) {
            if (isLast) TimeLineItemType.End(color)
            else TimeLineItemType.Triple(color)
        } else {
            if (isLast) TimeLineItemType.None() else TimeLineItemType.Vertical(color)
        }
    }
}
