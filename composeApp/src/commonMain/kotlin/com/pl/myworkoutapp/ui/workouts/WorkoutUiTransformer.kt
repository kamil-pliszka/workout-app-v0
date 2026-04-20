package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.theme.*


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
    val timeline = ancestors.mapIndexed { levelIndex, isParentLast ->
        val color = treeColors[levelIndex % treeColors.size]
        // If parent was NOT last, we continue the vertical line down
        if (isParentLast) TimeLineItemType.None() else TimeLineItemType.Vertical(color)
    }

    val currentColor = treeColors[level % treeColors.size]
    val lastItem = if (isLast) TimeLineItemType.End(currentColor)
    else TimeLineItemType.Triple(currentColor)

    return timeline + lastItem
}
/*
fun WorkoutTraversalItem.toTimeline2(): List<TimeLineItemType> {
    val result = mutableListOf<TimeLineItemType>()
    ancestors.forEachIndexed { levelIndex, ancestor ->
        val color = treeColors[levelIndex % treeColors.size]
        result.add(if (ancestor) TimeLineItemType.None() else TimeLineItemType.Vertical(color))
    }
    val color = treeColors[level % treeColors.size]
    result.add(if (isLast) TimeLineItemType.End(color) else TimeLineItemType.Triple(color))
    return result
}
*/

/**
 * Dokouje przekształcenia, ale tylko dla ćwiczeń typu builtin (te nie wymagają dostępu do repo)
 */
fun transform(workout: Workout): WorkoutWithExercisesUiModel {
    val exerciseIds = workout.items.extractExerciseIds()
    val exercises = exerciseIds.map { id ->
        when (id) {
            is ExerciseId.BuiltIn -> BuiltInExerciseRegistry.get((id).id)
            is ExerciseId.Custom -> error("Custom not supported")
        }
    }.toSet()
    return transform(workout, exercises)
}

fun transform(workout: Workout, exercises: Set<Exercise>): WorkoutWithExercisesUiModel {
    val exercisesMap = exercises.associateBy { it.id }
    val workoutUiModel = workout.toUi()
    val traversalItems = workout.items.flatten()

    //WorkoutItem(Circuit) doesn't have a unique ID, use unique index as key
    val itemsUiModel = traversalItems.mapIndexed { index, workoutTraversalItem ->
        when (workoutTraversalItem.item) {
            is Circuit -> workoutTraversalItem.item.toUiBase().copy(
                timeline = workoutTraversalItem.toTimeline(),
                key = index,
            )

            is WorkoutExercise -> workoutTraversalItem.item.toUiBase(
                exercisesMap.getValue(
                    workoutTraversalItem.item.exerciseId
                )
            ).copy(
                timeline = workoutTraversalItem.toTimeline(),
                key = index,
            )
        }
    }
    return WorkoutWithExercisesUiModel(
        workout = workoutUiModel,
        items = itemsUiModel
    )
}

private class CircuitBuilder(
    val phase: Phase,
    val name: String?,
    val structure: CircuitStructure,
) {
    val items = mutableListOf<WorkoutItem>()

    fun build(): Circuit {
        require(items.isNotEmpty()) {
            "Circuit must contain at least one item"
        }
        return Circuit(
            phase = phase,
            name = name,
            structure = structure,
            items = items.toList()
        )
    }
}

fun toDomain(items: List<WorkoutUiItem>): List<WorkoutItem> {
    val result = mutableListOf<WorkoutItem>()
    val stack = mutableListOf<CircuitBuilder>()

    items.forEach { uiItem ->
        val level = uiItem.timeline.size - 1
        println("level: $level, stack: ${stack.size}")
//        when(uiItem) {
//            is CircuitUiItem -> println("circuit: ${uiItem.phase}")
//            is ExerciseUiItem -> println("exercise: ${uiItem.exerciseId}")
//        }
        require(level <= stack.size) {
            "Invalid hierarchy: level=$level, stack=${stack.size}"
        }
        // schodzimy do odpowiedniego poziomu
        while (stack.size > level) {
            val finished = stack.removeAt(stack.lastIndex).build()
            if (stack.isEmpty()) {
                result.add(finished)
            } else {
                stack.last().items.add(finished)
            }
        }
        when (uiItem) {
            is CircuitUiItem -> {
                val builder = CircuitBuilder(
                    phase = uiItem.phase,
                    name = null, //TODO - name = uiItem.title.asString(), // dostosuj
                    structure = uiItem.structure
                )
                stack.add(builder)
            }

            is ExerciseUiItem -> {
                val exercise = WorkoutExercise(
                    exerciseId = uiItem.exerciseId,
                    quantity = Quantity(
                        uiItem.quantityType,
                        uiItem.quantityValue
                    )
                )

                if (stack.isEmpty()) {
                    result.add(exercise)
                } else {
                    stack.last().items.add(exercise)
                }
            }
        }
    }

    // domykamy wszystkie otwarte circuity
    while (stack.isNotEmpty()) {
        val finished = stack.removeAt(stack.lastIndex).build()
        if (stack.isEmpty()) {
            result.add(finished)
        } else {
            stack.last().items.add(finished)
        }
    }

    return result
}
