package com.pl.myworkoutapp.ui.workouts.tree

import com.pl.myworkoutapp.ui.workouts.*


sealed class TreeNode {
    abstract var parent: CircuitNode?
    abstract val key: Int
}

class ExerciseNode(
    override var parent: CircuitNode?,
    override val key: Int = exercise.key,
    val exercise: ExerciseUiItem,
) : TreeNode()

class CircuitNode(
    override var parent: CircuitNode?,
    override val key: Int = circuit.key,
    val circuit: CircuitUiItem,
    //mutable - bo nie chce dodawać pośredniegu buildera, to klasa wewnętrzna
    val children: MutableList<TreeNode> = mutableListOf()
) : TreeNode()

fun CircuitUiItem.toNode(parent: CircuitNode? = null) = CircuitNode(
    parent = parent,
    circuit = this,
)

fun ExerciseUiItem.toNode(parent: CircuitNode? = null) = ExerciseNode(
    parent = parent,
    exercise = this,
)


private fun MutableList<TreeNode>.attach(
    stack: List<CircuitNode>,
    node: TreeNode
) {
    if (stack.isEmpty()) {
        add(node)
    } else {
        stack.last().children.add(node)
    }
}

fun List<WorkoutUiItem>.toTree(): List<TreeNode> {
    val result = mutableListOf<TreeNode>()
    val stack = mutableListOf<CircuitNode>()

    forEach { uiItem ->
        require(uiItem.depth <= stack.size) {
            "Invalid hierarchy: depth=${uiItem.depth}, stack=${stack.size}"
        }

        while (stack.size > uiItem.depth) {
            val finished = stack.removeAt(stack.lastIndex)
            result.attach(stack, finished)
        }

        when (uiItem) {
            is ExerciseUiItem -> {
                result.attach(stack, uiItem.toNode(stack.lastOrNull()))
            }

            is CircuitUiItem -> {
                val node = uiItem.toNode(stack.lastOrNull())
                stack.add(node)
            }
        }
    }

    while (stack.isNotEmpty()) {
        val finished = stack.removeAt(stack.lastIndex)
        result.attach(stack, finished)
    }

    return result
}

fun List<TreeNode>.findNode(key: Int): TreeNode? {
    for (node in this) {
        if (node.key == key) return node

        if (node is CircuitNode) {
            val found = node.children.findNode(key)
            if (found != null) return found
        }
    }
    return null
}

fun TreeNode.isDescendantOf(ancestorKey: Int): Boolean {
    var current = parent
    while (current != null) {
        if (current.key == ancestorKey) return true
        current = current.parent
    }
    return false
}


