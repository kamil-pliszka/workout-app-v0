package com.pl.myworkoutapp.ui.workouts.tree

import com.pl.myworkoutapp.ui.common.DropPosition

class WorkoutTreeMutator {

    fun apply(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation
    ): List<TreeNode> {
        val roots = tree.toMutableList()

        return when (mutation) {
            is WorkoutTreeMutation.Move ->
                move(roots, mutation)

            is WorkoutTreeMutation.InsertExercise ->
                insertExercise(roots, mutation)

            is WorkoutTreeMutation.InsertCircuit ->
                insertCircuit(roots, mutation)

            is WorkoutTreeMutation.Delete ->
                delete(roots, mutation)

            is WorkoutTreeMutation.ReplaceExercise ->
                replaceExercise(roots, mutation)
        }
    }

    private fun move(
        roots: MutableList<TreeNode>,
        mutation: WorkoutTreeMutation.Move
    ): List<TreeNode> {
        val source = roots.findNode(mutation.draggedKey) ?: return roots
        val target = roots.findNode(mutation.targetKey) ?: return roots

        detach(roots, source)
        insert(roots, source, target, mutation.position)

        return roots
    }

    private fun insertExercise(
        roots: MutableList<TreeNode>,
        mutation: WorkoutTreeMutation.InsertExercise
    ): List<TreeNode> {
        val node = mutation.exercise.toNode()

        val targetKey = mutation.targetKey
        if (targetKey == null) {
            roots.add(node)
            return roots
        }

        val target = roots.findNode(targetKey) ?: return roots
        insert(roots, node, target, mutation.position)

        return roots
    }

    private fun insertCircuit(
        roots: MutableList<TreeNode>,
        mutation: WorkoutTreeMutation.InsertCircuit
    ): List<TreeNode> {
        val node = mutation.circuit.toNode()

        val targetKey = mutation.targetKey
        if (targetKey == null) {
            roots.add(node)
            return roots
        }

        val target = roots.findNode(targetKey) ?: return roots
        insert(roots, node, target, mutation.position)

        return roots
    }

    private fun delete(
        roots: MutableList<TreeNode>,
        mutation: WorkoutTreeMutation.Delete
    ): List<TreeNode> {
        val node = roots.findNode(mutation.key) ?: return roots
        detach(roots, node)
        return roots
    }


    private fun replaceExercise(
        roots: MutableList<TreeNode>,
        mutation: WorkoutTreeMutation.ReplaceExercise
    ): List<TreeNode> {
        val current = roots.findNode(mutation.key) as? ExerciseNode ?: return roots
        val replacement = mutation.newExercise.toNode(current.parent)

        replaceInParent(roots, current, replacement)
        return roots
    }

    private fun detach(
        roots: MutableList<TreeNode>,
        node: TreeNode
    ) {
        val parent = node.parent
        if (parent == null) {
            roots.remove(node)
        } else {
            parent.children.remove(node)
        }
    }

    private fun insert(
        roots: MutableList<TreeNode>,
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ) {
        when (position) {
            DropPosition.BEFORE -> insertBefore(roots, source, target)
            DropPosition.AFTER -> insertAfter(roots, source, target)
            DropPosition.INSIDE -> insertInside(source, target)
        }
    }

    private fun insertBefore(
        roots: MutableList<TreeNode>,
        source: TreeNode,
        target: TreeNode
    ) {
        val parent = target.parent
        source.parent = parent

        val siblings = parent?.children ?: roots
        val index = siblings.indexOf(target)
        siblings.add(index, source)
    }

    private fun insertAfter(
        roots: MutableList<TreeNode>,
        source: TreeNode,
        target: TreeNode
    ) {
        val parent = target.parent
        source.parent = parent

        val siblings = parent?.children ?: roots
        val index = siblings.indexOf(target)
        siblings.add(index + 1, source)
    }

    private fun insertInside(
        source: TreeNode,
        target: TreeNode
    ) {
        val circuit = target as CircuitNode
        source.parent = circuit
        circuit.children.add(source)
    }

    private fun replaceInParent(
        roots: MutableList<TreeNode>,
        current: TreeNode,
        replacement: TreeNode
    ) {
        val parent = current.parent

        if (parent == null) {
            val index = roots.indexOf(current)
            roots[index] = replacement
        } else {
            val index = parent.children.indexOf(current)
            parent.children[index] = replacement
        }
    }
}

/**
 * Tree mutations (operacje na drzewie)
 *
 * To jest osobna warstwa.
 *
 * Ona ma robić tylko operacje typu:
 *
 * znajdź node
 * remove node
 * insert before
 * insert after
 * insert inside
 * move node
 *
 * Bez wiedzy o UI i bez DragDropEvent.
 *
 * To powinno operować wyłącznie na: List<TreeNode>
 * i zwracać zmodyfikowane drzewo.
 */
//| Target   | BEFORE                 | INSIDE               | AFTER                 |
//| -------- | ---------------------- | -------------------- | --------------------- |
//| Exercise | sibling before         |                      | sibling after         |
//| Circuit  | sibling before circuit | append as last child | sibling after circuit |
@Suppress("unused")
class WorkoutTreeMutatorArchived {
    fun move(
        roots: MutableList<TreeNode>,
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): List<TreeNode> {

        val detached = detach(roots, source) ?: return roots
        insert(detached, target, position, roots)

        return roots
    }

    private fun detach(
        roots: MutableList<TreeNode>,
        node: TreeNode
    ): TreeNode? {
        val parent = node.parent

        return if (parent == null) {
            val removed = roots.removeByKey(node.key)
            removed?.apply { this.parent = null }
        } else {
            val removed = parent.children.removeByKey(node.key)
            removed?.apply { this.parent = null }
        }
    }

    private fun insert(
        node: TreeNode,
        target: TreeNode,
        position: DropPosition,
        roots: MutableList<TreeNode>
    ) {
        when (position) {
            DropPosition.BEFORE -> insertBefore(node, target, roots)
            DropPosition.AFTER -> insertAfter(node, target, roots)
            DropPosition.INSIDE -> insertInside(node, target)
        }
    }

    private fun insertBefore(
        node: TreeNode,
        target: TreeNode,
        roots: MutableList<TreeNode>
    ) {
        val parent = target.parent

        if (parent == null) {
            roots.insertBefore(target.key, node)
            node.parent = null
        } else {
            parent.children.insertBefore(target.key, node)
            node.parent = parent
        }
    }

    private fun insertAfter(
        node: TreeNode,
        target: TreeNode,
        roots: MutableList<TreeNode>
    ) {
        val parent = target.parent

        if (parent == null) {
            roots.insertAfter(target.key, node)
            node.parent = null
        } else {
            parent.children.insertAfter(target.key, node)
            node.parent = parent
        }
    }

    private fun insertInside(
        node: TreeNode,
        target: TreeNode
    ) {
        require(target is CircuitNode) {
            "INSIDE is only valid for CircuitNode"
        }

        target.children.add(node)
        node.parent = target
    }

}

private fun MutableList<TreeNode>.removeByKey(key: Int): TreeNode? {
    val index = indexOfFirst { it.key == key }
    if (index == -1) return null
    return removeAt(index)
}

private fun MutableList<TreeNode>.insertBefore(targetKey: Int, node: TreeNode) {
    val index = indexOfFirst { it.key == targetKey }
    if (index == -1) add(node) else add(index, node)
}

private fun MutableList<TreeNode>.insertAfter(targetKey: Int, node: TreeNode) {
    val index = indexOfFirst { it.key == targetKey }
    if (index == -1) {
        add(node)
    } else {
        add(index + 1, node)
    }
}