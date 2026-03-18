package com.pl.myworkoutapp.domain.model.exercise

import com.pl.myworkoutapp.core.associateByUnique
import com.pl.myworkoutapp.domain.model.exercise.builtin.ABS_EXERCISES
import com.pl.myworkoutapp.domain.model.exercise.builtin.ARM_EXERCISES
import com.pl.myworkoutapp.domain.model.exercise.builtin.BACK_EXERCISES
import com.pl.myworkoutapp.domain.model.exercise.builtin.CHEST_EXERCISES
import com.pl.myworkoutapp.domain.model.exercise.builtin.CORE_EXERCISES
import com.pl.myworkoutapp.domain.model.exercise.builtin.LEGS_EXERCISES

object BuiltInExerciseRegistry {
    private val BUILT_IN_EXERCISES = (
            ABS_EXERCISES
                    + ARM_EXERCISES
                    + BACK_EXERCISES
                    + CHEST_EXERCISES
                    + CORE_EXERCISES
                    + LEGS_EXERCISES
            ).associateByUnique { it.id.toBuiltInExerciseId() }

    //private val CUSTOM_EXERCISES = mutableMapOf<Int, CustomExercise>()

    fun get(id: BuiltInExerciseId) = BUILT_IN_EXERCISES[id] ?: error("Missing built-in: $id")
    fun getAllId() : Set<BuiltInExerciseId> = BUILT_IN_EXERCISES.keys

    //kod dotyczący CustomExercise będzie przeniesiony do jakiegoś repo w przyszłości
    //fun getCustomExercise(id: Int) = CUSTOM_EXERCISES[id]
    //fun getExercise(id: ExerciseId): Exercise? = when (id) {
    //    is ExerciseId.BuiltIn -> getBuiltInExercise(id.toBuiltInExerciseId())
    //    is ExerciseId.Custom -> getCustomExercise(id.toInt())
    //}
    //fun registerCustom(exercise: CustomExercise) = CUSTOM_EXERCISES.put(exercise.id.id, exercise)

//
//    init {
//        //tymczasowe wpisy
//        registerCustom(
//            CustomExercise(
//                id = (-1).asExerciseId(),
//                name = "moje najlepsze ćwiczenie",
//                imageUri = null,
//                muscle = MuscleGroup.ABS,
//                exerciseType = ExerciseType.CARDIO,
//                equipment = Equipment.BODYWEIGHT,
//                met = 1.0,
//                quantityType = QuantityType.Reps,
//            )
//        )
//        registerCustom(
//            CustomExercise(
//                id = (-2).asExerciseId(),
//                name = "moje najlepsze ćwiczenie ramiona",
//                imageUri = "jakiś uri do nieistniejącego obrazka",
//                muscle = MuscleGroup.ARMS,
//                exerciseType = ExerciseType.MOBILITY,
//                equipment = Equipment.BODYWEIGHT,
//                met = 1.5,
//                quantityType = QuantityType.Reps,
//            )
//        )
//    }

    init {
        val missing = BuiltInExerciseId.entries - BUILT_IN_EXERCISES.keys
        require(missing.isEmpty()) {
            "Missing built-in exercises: $missing"
        }
    }
}


