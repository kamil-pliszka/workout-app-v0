package com.pl.myworkoutapp.ui.workouts

class WorkoutTreeMutationHandler(
    private val policy: WorkoutTreePolicy = WorkoutTreePolicy(),
    private val mutator: WorkoutTreeMutator = WorkoutTreeMutator(),
    private val normalizer: WorkoutTreeNormalizer = WorkoutTreeNormalizer(),
) {
    fun apply(
        workout: WorkoutWithExercisesUiModel,
        mutation: WorkoutTreeMutation
    ): WorkoutWithExercisesUiModel {
        val tree = workout.items.toTree()

        if (!policy.canApply(tree, mutation)) return workout

        val mutated = mutator.apply(tree, mutation)
        return normalizer.normalize(workout, mutated)
    }

}