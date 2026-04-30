package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

@Composable
fun TimelinePartDashed(
    isLast: Boolean,
    themeColor: Color,
    dashColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Box(
        modifier = Modifier.width(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier.fillMaxHeight()
            //modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 4f
            val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            if (!isLast) {
                drawLine(
                    color = dashColor,
                    start = Offset(size.width / 2, 20f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = strokeWidth,
                    pathEffect = dash
                )
            }
        }
        // 🔵 kropka
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(12.dp)
                .background(themeColor, CircleShape)
        )
    }
}