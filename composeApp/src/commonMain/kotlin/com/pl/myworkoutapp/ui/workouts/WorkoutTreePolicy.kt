package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.ui.common.DropPosition

class WorkoutTreePolicy {

    fun canApply(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation
    ): Boolean {
        return when (mutation) {
            is WorkoutTreeMutation.Move ->
                canMove(tree, mutation)

            is WorkoutTreeMutation.InsertExercise ->
                canInsertExercise(tree, mutation)

            is WorkoutTreeMutation.InsertCircuit ->
                canInsertCircuit(tree, mutation)

            is WorkoutTreeMutation.Delete ->
                canDelete(tree, mutation)

            is WorkoutTreeMutation.ReplaceExercise ->
                canReplaceExercise(tree, mutation)
        }
    }

    private fun canMove(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation.Move
    ): Boolean {
        val source = tree.findNode(mutation.draggedKey) ?: return false
        val target = tree.findNode(mutation.targetKey) ?: return false

        if (source.key == target.key) return false
        if (target.isDescendantOf(source.key)) return false

        return when (mutation.position) {
            DropPosition.BEFORE -> canInsertSibling(source, target)
            DropPosition.AFTER -> canInsertSibling(source, target)
            DropPosition.INSIDE -> canInsertInside(source, target)
        }
    }

    private fun canInsertExercise(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation.InsertExercise
    ): Boolean {
        val targetKey = mutation.targetKey ?: return true // append root

        val target = tree.findNode(targetKey) ?: return false

        return when (mutation.position) {
            DropPosition.BEFORE -> true
            DropPosition.AFTER -> true
            DropPosition.INSIDE -> target is CircuitNode
        }
    }

    private fun canInsertCircuit(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation.InsertCircuit
    ): Boolean {
        val targetKey = mutation.targetKey ?: return true // append root

        val target = tree.findNode(targetKey) ?: return false

        return when (mutation.position) {
            DropPosition.BEFORE -> true
            DropPosition.AFTER -> true
            DropPosition.INSIDE -> target is CircuitNode && target.canContainCircuit()
        }
    }

    private fun CircuitNode.canContainCircuit() : Boolean {
        //TODO - do zaimplementowania kiedys
        //I tu już masz miejsce na reguły typu:
        //tabata nie może zawierać circuit
        //emom nie może mieć nested circuit
        //root może
        //standard circuit może
        return true
    }

    private fun canDelete(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation.Delete
    ): Boolean {
        val node = tree.findNode(mutation.key) ?: return false

        return when (node) {
            is ExerciseNode -> true
            is CircuitNode -> true // albo np. node.children.isNotEmpty()
        }
    }

    private fun canReplaceExercise(
        tree: List<TreeNode>,
        mutation: WorkoutTreeMutation.ReplaceExercise
    ): Boolean {
        val node = tree.findNode(mutation.key) ?: return false
        return node is ExerciseNode
    }

    private fun canInsertInside(
        source: TreeNode,
        target: TreeNode
    ): Boolean {
        if (target !is CircuitNode) return false

        return when (target.circuit.structure) {
            is CircuitStructure.Standard -> true
            is CircuitStructure.Tabata -> source is ExerciseNode
            is CircuitStructure.EMOM -> source is ExerciseNode
            is CircuitStructure.AMRAP -> source is ExerciseNode
        }
    }

    private fun canInsertSibling(
        source: TreeNode,
        target: TreeNode
    ): Boolean {
        val targetParent = target.parent

        return if (targetParent == null) {
            // target is root-level
            canInsertAtRoot(source)
        } else {
            canInsertIntoParent(source, targetParent)
        }
    }

    private fun canInsertAtRoot(@Suppress("unused") source: TreeNode): Boolean {
        return true
    }

    private fun canInsertIntoParent(
        source: TreeNode,
        parent: CircuitNode
    ): Boolean {
        return when (parent.circuit.structure) {
            is CircuitStructure.Standard -> true
            is CircuitStructure.Tabata -> source is ExerciseNode
            is CircuitStructure.EMOM -> source is ExerciseNode
            is CircuitStructure.AMRAP -> source is ExerciseNode
        }
    }
}