package com.pl.myworkoutapp.ui.plans.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.ProgressCircle
import com.pl.myworkoutapp.ui.plans.*
import com.pl.myworkoutapp.ui.theme.DesertWhite
import com.pl.myworkoutapp.ui.theme.FernGreen
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DayCard(
    day: PlanDayUiModel,
    themeColor: Color,
    onStartClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            DesertWhite,
                            DesertWhite.copy(alpha = 0.7f)
                        )
                    ),
                    shape = cardShape
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.plans_day_number, day.dayIndex),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = day.desc.asString(),
                            style = MaterialTheme.typography.titleSmall,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when (day.dayType) {
                            DayType.Rest -> {
                                Image(
                                    painter = painterResource(if (day.dayIndex % 2 == 0) Res.drawable.ic_rest_day1 else Res.drawable.ic_rest_day2),
                                    contentDescription = "Rest day icon",
                                    modifier = Modifier
                                )
                            }

                            DayType.Workout -> {
                                //można coś pokazać
                            }
                        }
                        when (day.dayProgress) {
                            DayProgress.Done -> {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_done_all),
                                    contentDescription = "Well done",
                                    tint = themeColor,
                                    modifier = Modifier
                                )
                            }

                            is DayProgress.InProgress -> {
                                ProgressCircle(
                                    progress = day.dayProgress.progress,
                                    strokeWidth = 5.dp,
                                    trackColor = themeColor,
                                    modifier = Modifier,
                                    size = 40.dp
                                )
                            }

                            DayProgress.NotStarted -> {
                                //nic
                            }
                        }
                    }
                }

                if (day.isCurrent && day.dayProgress !is DayProgress.Done) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onStartClick,
                        modifier = Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(50),
                        colors = buttonColors(containerColor = themeColor)
                    ) {
                        Text(
                            text = if (day.dayProgress is DayProgress.InProgress) {
                                stringResource(Res.string.plans_day_resume)
                            } else /*if (day.dayProgress is DayProgress.NotStarted)*/ {
                                stringResource(Res.string.plans_day_start)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DayCardPreviewRestDay1() {
    DayCard(
        PlanDayUiModel(
            desc = "Dzien odpoczynku".asUiText(),
            dayIndex = 13,
            dayType = DayType.Rest,
            dayProgress = DayProgress.NotStarted,
            isCurrent = false,
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}

@Preview
@Composable
fun DayCardPreviewRestDay2() {
    DayCard(
        PlanDayUiModel(
            desc = "Dzien laby".asUiText(),
            dayIndex = 14,
            dayType = DayType.Rest,
            dayProgress = DayProgress.Done,
            isCurrent = false,
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}

@Preview
@Composable
fun DayCardPreviewDone() {
    DayCard(
        PlanDayUiModel(
            desc = "Dzień ćwiczeń zrobiony".asUiText(),
            dayIndex = 12,
            dayProgress = DayProgress.Done,
            dayType = DayType.Workout,
            isCurrent = false,
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}


@Preview
@Composable
fun DayCardPreviewDoneRest() {
    DayCard(
        PlanDayUiModel(
            desc = "Dzień odpoczynku zrobiony".asUiText(),
            dayIndex = 12,
            dayProgress = DayProgress.Done,
            dayType = DayType.Rest,
            isCurrent = false,
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}

@Preview
@Composable
fun DayCardPreviewCurrentStart() {
    DayCard(
        PlanDayUiModel(
            desc = "Ciężki dzień".asUiText(),
            dayIndex = 14,
            isCurrent = true,
            dayType = DayType.Workout,
            dayProgress = DayProgress.NotStarted,
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}

@Preview
@Composable
fun DayCardPreviewCurrentResume() {
    DayCard(
        PlanDayUiModel(
            desc = "Ciężki dzień".asUiText(),
            dayIndex = 14,
            isCurrent = true,
            dayType = DayType.Workout,
            dayProgress = DayProgress.InProgress(0.37f),
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}

@Preview
@Composable
fun DayCardPreviewProgressNotCurrent() {
    DayCard(
        PlanDayUiModel(
            desc = "W trakcie".asUiText(),
            dayIndex = 10,
            isCurrent = false,
            dayType = DayType.Workout,
            dayProgress = DayProgress.InProgress(0.41f),
        ),
        themeColor = FernGreen,
        onStartClick = { }
    )
}