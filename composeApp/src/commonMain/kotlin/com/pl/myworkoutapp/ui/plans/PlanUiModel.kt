package com.pl.myworkoutapp.ui.plans

import androidx.compose.ui.graphics.Color
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.ui.common.UiText
import org.jetbrains.compose.resources.DrawableResource

data class PlanUiModel(
    val id: String,
    val name: UiText,
    val desc: UiText,
    val imageUrl: DrawableResource,
    val days: List<PlanDayUiModel>,
    val isInProgress: Boolean,
    val difficulty: Difficulty,
    val themeColor: Color,
)

sealed interface DayType {
    object Workout : DayType
    object Rest : DayType
}

sealed interface DayProgress {
    object NotStarted : DayProgress
    data class InProgress(val progress: Float) : DayProgress
    object Done : DayProgress
}


data class PlanDayUiModel(
    val desc: UiText,
    val dayIndex: Int,
    val dayType: DayType,
    val dayProgress: DayProgress,
    val isCurrent: Boolean,
)
