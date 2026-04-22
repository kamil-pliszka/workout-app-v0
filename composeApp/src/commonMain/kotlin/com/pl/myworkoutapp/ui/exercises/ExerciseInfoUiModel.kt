package com.pl.myworkoutapp.ui.exercises

import androidx.compose.ui.text.intl.Locale
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.loadExerciseDescription
import org.jetbrains.compose.resources.DrawableResource


data class ExerciseInfoUiModel(
    val exerciseId: ExerciseId,
    val muscle: MuscleGroup,
    val quantityType: QuantityType,
    val quantityValue: Int? = null,
    val quantityDirty: Boolean = false,
    val equipment: Equipment,
    val name: UiText,
    val customDesc: UiText?,
    val descExerciseId: BuiltInExerciseId?,//
    val descriptionMarkdown: String?,
    val icon: DrawableResource?,
    val imagePath: String?,
    val current: Int? = null,
    val total: Int? = null,
)

suspend fun ExerciseInfoUiModel.loadExerciseDescription(): String? {
    return this.takeIf { it.customDesc == null }
        ?.descExerciseId
        ?.let { id ->
            loadExerciseDescription(
                exerciseId = id,
                lang = Locale.current.language
            )
        }
}