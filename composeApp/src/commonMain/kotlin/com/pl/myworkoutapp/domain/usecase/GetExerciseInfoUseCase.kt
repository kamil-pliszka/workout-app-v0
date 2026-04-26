package com.pl.myworkoutapp.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.loadExerciseDescription


data class ExerciseWithMarkdown(
    val exercise: Exercise,
    val descriptionMarkdown: String?,
)
class GetExerciseInfoUseCase(
    private val repository: WorkoutRepository
) {
    suspend fun execute(exerciseId: ExerciseId): ExerciseWithMarkdown {
        val exercise: Exercise = repository.getExercise(exerciseId)
        /*val customDesc = when(exercise) {
            is BuiltInExercise -> null
            is CustomExercise ->  when {
                !exercise.description.isNullOrBlank() -> exercise.description
                else -> null
            }
        }*/
        val descExerciseId: BuiltInExerciseId? = when(exercise) {
            is BuiltInExercise -> exercise.id.toBuiltInExerciseId()
            is CustomExercise -> exercise.basedOn?.toBuiltInExerciseId()
        }
        val descMarkdown = descExerciseId?.let {
            loadExerciseDescription(
                exerciseId = descExerciseId,
                lang = Locale.current.language
            )
        }

        return ExerciseWithMarkdown(
            exercise = exercise,
            descriptionMarkdown = descMarkdown
        )
    }
}