package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.exercise.ExerciseId

fun List<WorkoutItem>.extractExerciseIds(): Set<ExerciseId> {
    return buildSet {
        for (item in this@extractExerciseIds) {
            when (item) {
                is Circuit -> addAll(item.items.extractExerciseIds())
                is WorkoutExercise -> add(item.exerciseId)
            }
        }
    }
}