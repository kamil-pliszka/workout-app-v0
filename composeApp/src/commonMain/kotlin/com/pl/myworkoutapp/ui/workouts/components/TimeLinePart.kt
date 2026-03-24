package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.workouts.TimeLineItemType


@Composable
fun TimeLinePart(
    types: List<TimeLineItemType> = listOf(),
    strokeWidth: Dp = 2.5.dp,
    itemWidth: Dp = 24.dp
) {
    Row {
        types.forEach { type ->
            Box(
                modifier = Modifier.width(itemWidth).fillMaxHeight()
                    .drawBehind {
                        val path = Path().apply {
                            val startX = size.width / 2f
                            //based on https://gist.github.com/fabiosassu/603a4634513933accb2503510e3940ed
                            when (type) {
                                is TimeLineItemType.Vertical -> {
                                    moveTo(startX, 0f)
                                    lineTo(startX, size.height)
                                }
                                is TimeLineItemType.Triple -> {
                                    moveTo(startX, 0f)
                                    lineTo(startX, size.height)
                                    moveTo(startX, size.height/2f)
                                    lineTo(size.width, size.height / 2f)
                                }

                                is TimeLineItemType.End -> {
                                    moveTo(startX, 0f)
                                    lineTo(startX, size.height/2f)
                                    lineTo(size.width, size.height / 2f)
                                }

                                is TimeLineItemType.None -> {
                                    //empty
                                }
                            }
                        }
                        drawPath(
                            path,
                            style = Stroke(
                                width = strokeWidth.toPx(),
                            ),
                            color = type.color
                        )
                    },
            )
        }
    }
}

@Preview
@Composable
fun TimeLinePreview1() {
    Box(Modifier.height(40.dp)) {
        TimeLinePart(
            listOf(
                TimeLineItemType.Vertical(Color.Green),
                TimeLineItemType.None(),
                TimeLineItemType.End(Color.Red),
            )
        )
    }
}

@Preview
@Composable
fun TimeLinePreview2() {
    Box(Modifier.height(40.dp)) {
        TimeLinePart(
            listOf(
                TimeLineItemType.Vertical(Color.Green),
                TimeLineItemType.Triple(Color.Magenta),
                TimeLineItemType.Vertical(Color.Red),
            )
        )
    }
}