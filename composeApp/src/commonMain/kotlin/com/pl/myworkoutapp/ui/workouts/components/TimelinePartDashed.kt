package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
) {
    Box(
        modifier = Modifier.Companion.width(24.dp),
        contentAlignment = Alignment.Companion.TopCenter
    ) {
        Canvas(
            modifier = Modifier.Companion.fillMaxHeight()
            //modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 4f
            val dash = PathEffect.Companion.dashPathEffect(floatArrayOf(10f, 10f))
            if (!isLast) {
                drawLine(
                    color = Color.Companion.LightGray,
                    start = Offset(size.width / 2, 20f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = strokeWidth,
                    pathEffect = dash
                )
            }
        }
        // 🔵 kropka
        Box(
            modifier = Modifier.Companion
                .padding(top = 10.dp)
                .size(12.dp)
                .background(themeColor, CircleShape)
        )
    }
}