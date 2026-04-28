package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.ui.common.DropPosition

/**
 * Ma tylko 2 odpowiedzialności:
 * normalize
 * normalizuje input z UI do semantyki drzewa
 * isValid
 * sprawdza, czy taki drop wolno wykonać
 */
class WorkoutDropPolicyArchived {
    private val TAG = "WorkoutDropPolicy"
    /**
     * UI może zwrócić pozycję, która semantycznie nie ma sensu dla targetu.
     * Normalizujemy ją zanim zaczniemy walidację / mutację.
     * Rules:
     * - Exercise:
     *   - BEFORE -> BEFORE
     *   - INSIDE -> AFTER   (exercise nie ma children)
     *   - AFTER  -> AFTER
     *
     * - Circuit:
     *   - BEFORE -> BEFORE
     *   - INSIDE -> INSIDE  (append as last child)
     *   - AFTER  -> AFTER
     */
    fun normalize(
        @Suppress("unused") source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): DropPosition {
        return when (target) {
            is ExerciseNode -> {
                when (position) {
                    DropPosition.INSIDE -> DropPosition.AFTER
                    DropPosition.BEFORE -> DropPosition.BEFORE
                    DropPosition.AFTER -> DropPosition.AFTER
                }
            }

            is CircuitNode -> position
        }
    }

    /**
     * Walidacja reguł biznesowych DnD.
     *
     * Reject:
     * - drop na samego siebie
     * - drop do własnego subtree
     * - drop tworzący cykl
     * - no-op (opcjonalnie traktowany jako invalid)
     */
    fun isValid(
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): Boolean {
        return isStructurallyValid(source, target, position) &&
                isBusinessValid(source, target, position)
    }

    private fun isStructurallyValid(
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): Boolean {
        // self drop
        if (source.key == target.key) return false

        // cannot drop into own subtree (cycle)
        if (target.isDescendantOf(source.key)){
            Log.d(TAG, "target.isDescendant")
            return false
        }

        // optional no-op rejection
        if (isNoOp(source, target, position)) {
            Log.d(TAG, "no-op rejection")
            return false
        }

        return true
    }

    /**
     * Technicznie legalne, ale nic nie zmieniające.
     *
     * Przykłady:
     * - A BEFORE B, gdy A już stoi bezpośrednio przed B w tym samym parent
     * - A AFTER B, gdy A już stoi bezpośrednio za B w tym samym parent
     *
     * To można odrzucić wcześnie, żeby nie robić pustych rebuildów.
     */
    private fun isNoOp(
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): Boolean {
        if (source.parent != target.parent) {
            Log.d(TAG, "!= parent")
            return false
        }

        val siblings = source.parent?.children ?: return false

        val sourceIndex = siblings.indexOfFirst { it.key == source.key }
        val targetIndex = siblings.indexOfFirst { it.key == target.key }

        if (sourceIndex == -1 || targetIndex == -1) {
            Log.d(TAG, "key not found in siblings")
            return false
        }

        return when (position) {
            DropPosition.BEFORE -> sourceIndex == targetIndex - 1
            DropPosition.AFTER -> sourceIndex == targetIndex + 1
            DropPosition.INSIDE -> false
        }
    }

    private fun isBusinessValid(
        source: TreeNode,
        target: TreeNode,
        position: DropPosition
    ): Boolean {
        val targetCircuit = when {
            position == DropPosition.INSIDE && target is CircuitNode -> target
            else -> target.parent
        } ?: return true

        //TODO - doimplementować kiedyś
        return when (targetCircuit.circuit.structure) {
            is CircuitStructure.Tabata -> source is ExerciseNode
            is CircuitStructure.EMOM -> source is ExerciseNode
            is CircuitStructure.AMRAP -> source is ExerciseNode
            is CircuitStructure.Standard -> true
        }
    }
}