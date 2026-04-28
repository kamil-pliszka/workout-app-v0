package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.workouts.tree.CircuitNode
import com.pl.myworkoutapp.ui.workouts.tree.ExerciseNode
import com.pl.myworkoutapp.ui.workouts.tree.toTree
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Testuje: flat -> tree
 * Czyli:
 *
 * root exercise
 * root circuit
 * nested exercise
 * nested circuit
 * mixed hierarchy
 * invalid hierarchy - tego na razie nie ma
 */
class WorkoutTreeBuilderTest {
    //private val handler = WorkoutDropHandler()

    @Test
    fun builds_tree_from_flat_simple_0() {
        val flat = listOf(
            exercise(key = 1, depth = 0),
        )
        val tree = flat.toTree()

        assertEquals(1, tree.size)
        assertEquals(1, tree[0].key)
        assertTrue { tree[0] is ExerciseNode }
        assertTrue { tree[0].parent == null }
    }

    @Test
    fun builds_tree_from_flat_simple_1() {
        val flat = listOf(
            circuit(key = 1, depth = 0),
            exercise(key = 2, depth = 1),
        )
        val tree = flat.toTree()

        assertEquals(1, tree.size)
        assertEquals(1, tree[0].key)
        assertTrue { tree[0].parent == null }
        val circuit = tree[0] as CircuitNode

        assertEquals(1, circuit.children.size)
        val item = circuit.children[0] as ExerciseNode
        assertEquals(2, item.key)
    }


    @Test
    fun builds_tree_from_flat_structure() {
        val flat = listOf(
            exercise(key = 1, depth = 0),
            circuit(key = 2, depth = 0),
            exercise(key = 3, depth = 1),
            exercise(key = 4, depth = 1),
            exercise(key = 5, depth = 0),
        )

        val tree = flat.toTree()

        assertEquals(3, tree.size)
        assertEquals(1, tree[0].key)
        assertEquals(2, tree[1].key)
        assertEquals(5, tree[2].key)

        val circuit = tree[1] as CircuitNode
        assertEquals(2, circuit.children.size)
        assertEquals(3, circuit.children[0].key)
        assertEquals(4, circuit.children[1].key)
    }

    @Test
    fun build_from_flat_test_2() {
        // L0  L1  L2  L3 - level
        // E1
        // C2
        //  |--C3
        //      |--C4
        //          |--E5
        val flat = listOf(
            exercise(key = 1, depth = 0),
            circuit(key = 2, depth = 0),
            circuit(key = 3, depth = 1),
            circuit(key = 4, depth = 2),
            exercise(key = 5, depth = 3),
        )

        val tree = flat.toTree()
        assertEquals(2, tree.size)
        assertEquals(1, tree[0].key)
        assertEquals(2, tree[1].key)

        val circuit0 = tree[1] as CircuitNode
        assertEquals(1, circuit0.children.size)

        val circuit1 = circuit0.children[0] as CircuitNode
        assertEquals(3, circuit1.key)
        assertEquals(1, circuit1.children.size)

        val circuit2 = circuit1.children[0] as CircuitNode
        assertEquals(4, circuit2.key)
        assertEquals(1, circuit2.children.size)

        val item = circuit2.children[0]
        assertTrue { item is ExerciseNode }
    }


    @Test
    fun build_from_flat_test_3() {
        // L0  L1  L2  L3 - level
        // E1
        // C2
        //  |--C3
        //  |   |--C4
        //  |       |--E5
        //  |--E6
        val flat = listOf(
            exercise(key = 1, depth = 0),
            circuit(key = 2, depth = 0),
            circuit(key = 3, depth = 1),
            circuit(key = 4, depth = 2),
            exercise(key = 5, depth = 3),
            exercise(key = 6, depth = 1),
        )

        val tree = flat.toTree()
        assertEquals(2, tree.size)
        assertEquals(1, tree[0].key)
        assertTrue { tree[0].parent == null }

        val circuit2 = tree[1] as CircuitNode
        assertEquals(2, circuit2.key)
        assertEquals(2, circuit2.children.size)
        assertTrue { circuit2.parent == null }

        val circuit3 = circuit2.children[0] as CircuitNode
        val exercise6 = circuit2.children[1] as ExerciseNode
        assertEquals(3, circuit3.key)
        assertEquals(1, circuit3.children.size)
        assertTrue { circuit3.parent == circuit2 }
        assertEquals(6, exercise6.key)
        assertTrue { exercise6.parent == circuit2 }

        val circuit4 = circuit3.children[0] as CircuitNode
        assertEquals(4, circuit4.key)
        assertEquals(1, circuit4.children.size)
        assertTrue { circuit4.parent == circuit3 }

        val exercise5 = circuit4.children[0] as ExerciseNode
        assertEquals(5, exercise5.key)
        assertTrue { exercise5.parent == circuit4 }
    }
}