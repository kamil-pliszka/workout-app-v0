package com.pl.myworkoutapp.domain.model.exercise

sealed interface Exercise {
    val id: ExerciseId
    val muscle: MuscleGroup
    val exerciseType: ExerciseType
    val equipment: Equipment
    //użycie double jest celowe, nie chce wszedzie w kodzie dodawać suffixu f
    //https://en.wikipedia.org/wiki/Metabolic_equivalent_of_task
    val met: Double
    val quantityType: QuantityType
}
