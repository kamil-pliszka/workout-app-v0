package com.pl.myworkoutapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun ProgressCircle(
    progress: Float, // 0f - 1f
    strokeWidth: Dp = 4.dp,
    trackColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            strokeWidth = strokeWidth,
            color = trackColor,
            trackColor = MaterialTheme.colorScheme.outlineVariant,//Color.LightGray,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}