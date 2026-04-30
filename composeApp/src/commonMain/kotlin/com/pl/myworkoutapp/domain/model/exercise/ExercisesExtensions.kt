package com.pl.myworkoutapp.domain.model.exercise

fun Set<Exercise>.toMap() : Map<ExerciseId, Exercise> = this.associateBy { it.id }