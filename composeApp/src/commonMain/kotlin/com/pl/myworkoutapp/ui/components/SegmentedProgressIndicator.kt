package com.pl.myworkoutapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.plans.components.PREVIEW_CARD_PLAN
import com.pl.myworkoutapp.ui.plans.components.PlanCard


//https://www.francescvilarino.com/segmented-progress-bar


private const val BackgroundOpacity = 0.25f
private const val NumberOfSegments = 8
private val ProgressHeight = 4.dp
private val SegmentGap = 8.dp

@Composable
fun SegmentedProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = color.copy(alpha = BackgroundOpacity),
    progressHeight: Dp = ProgressHeight,
    numberOfSegments: Int = NumberOfSegments,
    segmentGap: Dp = SegmentGap
) {
    check(progress in 0f..1f) { "Invalid progress $progress" }
    check(numberOfSegments > 0) { "Number of segments must be greater than 0" }

    val gap: Float
    val barHeight: Float
    with(LocalDensity.current) {
        gap = segmentGap.toPx()
        barHeight = progressHeight.toPx()
    }
    Canvas(
        modifier
            .progressSemantics(progress)
            .height(progressHeight)
    ) {
        drawSegments(1f, backgroundColor, barHeight, numberOfSegments, gap)
        drawSegments(progress, color, barHeight, numberOfSegments, gap)
    }
}

private fun DrawScope.drawSegments(
    progress: Float,
    color: Color,
    segmentHeight: Float,
    segments: Int,
    segmentGap: Float,
) {
    val width = size.width
    val gaps = (segments - 1) * segmentGap
    val segmentWidth = (width - gaps) / segments
    val barsWidth = segmentWidth * segments
    val start: Float
    val end: Float

    val isLtr = layoutDirection == LayoutDirection.Ltr
    if (isLtr) {
        start = 0f
        end = barsWidth * progress + (progress * segments).toInt() * segmentGap
    } else {
        start = width
        end = (width - (barsWidth * progress + (progress * (segments - 1)).toInt() * segmentGap))
    }

    repeat(segments) { index ->
        val offset = index * (segmentWidth + segmentGap)
        val segmentStart: Float
        val segmentEnd: Float
        if (isLtr) {
            segmentStart = start + offset
            segmentEnd = (segmentStart + segmentWidth).coerceAtMost(end)
        } else {
            segmentEnd = width - offset
            segmentStart = (segmentEnd - segmentWidth).coerceAtLeast(end)
        }
        if (isLtr && offset <= end || !isLtr && segmentEnd > end) {
            drawRect(
                color,
                Offset(segmentStart, 0f),
                Size(segmentEnd - segmentStart, segmentHeight)
            )
        }
    }
}


@Preview
@Composable
fun SegmentedProgressIndicatorPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        SegmentedProgressIndicator(
            progress = 0.37f,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Magenta,
            //backgroundColor = Color.LightGray,
            progressHeight = 32.dp,
            numberOfSegments = 3,
            segmentGap = 16.dp
        )

    }
}