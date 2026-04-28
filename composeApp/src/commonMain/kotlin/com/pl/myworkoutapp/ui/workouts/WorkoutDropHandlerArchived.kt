package com.pl.myworkoutapp.ui.workouts

import com.pl.myworkoutapp.ui.common.DragDropEvent
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeMutator


/**
 * 4. Orchestrator (public entrypoint)
 * I on powinien być cienki.
 * Ma tylko:
 *
 * zbudować tree
 * znaleźć source/target
 * zapytać policy
 * odpalić mutator
 * znormalizować wynik do UI
 *
 * Nic więcej.
 */
class WorkoutDropHandlerArchived(
    private val policy: WorkoutDropPolicyArchived = WorkoutDropPolicyArchived(),
    private val mutator: WorkoutTreeMutator = WorkoutTreeMutator(),
) {

    fun drop(
        workout: WorkoutWithExercisesUiModel,
        event: DragDropEvent
    ): WorkoutWithExercisesUiModel {
        return workout
        /*
        val tree = workout.items.toTree()

        val source = tree.findNode(event.draggedKey) ?: return workout
        val target = tree.findNode(event.targetKey) ?: return workout

        val position = policy.normalize(source, target, event.position)

        if (!policy.isValid(source, target, position)) return workout

        val moved = mutator.move(tree.toMutableList(), source, target, position)

        return workout.copy(
            //Po drop trzeba zrobić normalizację
            //Musisz przeliczyć:
            //depth, parentKey, timeline
            items = moved.normalizeToUi()
        )
        */
    }
}

