package com.pl.myworkoutapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.difficulty_advanced
import myworkoutapplication.composeapp.generated.resources.difficulty_beginner
import myworkoutapplication.composeapp.generated.resources.difficulty_intermediate

data class DifficultyPresentation(
    val text: UiText,
    val color: Color
)

//TODO - kolory
fun Difficulty.toPresentation(): DifficultyPresentation = when (this) {
    Difficulty.BEGINNER -> DifficultyPresentation(
        text = Res.string.difficulty_beginner.asUiText(),
        color = Color(0xFF4CAF50) // zielony
    )
    Difficulty.INTERMEDIATE -> DifficultyPresentation(
        text = Res.string.difficulty_intermediate.asUiText(),
        color = Color(0xFFFF9800) // pomarańczowy
    )
    Difficulty.ADVANCED -> DifficultyPresentation(
        text = Res.string.difficulty_advanced.asUiText(),
        color = Color(0xFFF44336) // czerwony
    )
}

@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val (text, color) = difficulty.toPresentation()

    Row(
        modifier = modifier
            .background(color, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}