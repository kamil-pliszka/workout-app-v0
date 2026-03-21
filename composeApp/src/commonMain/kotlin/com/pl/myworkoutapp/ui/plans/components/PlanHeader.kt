package com.pl.myworkoutapp.ui.plans.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.DifficultyBadge
import com.pl.myworkoutapp.ui.plans.PlanUiModel
import com.pl.myworkoutapp.ui.theme.DarkBlue
import com.pl.myworkoutapp.ui.theme.holoRed
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_plank1
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlanHeader(
    plan: PlanUiModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(listOf(
                    plan.themeColor,
                    plan.themeColor.copy(alpha = 0.7f))
                )
            )
    ) {
        // IMAGE (po prawej)
        Image(
            painter = painterResource(plan.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .fillMaxHeight()
        )

        //overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // CONTENT (po lewej)
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            DifficultyBadge(plan.difficulty)

            Spacer(Modifier.height(8.dp))

            Text(
                text = plan.name.asString(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = plan.desc.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

val PREVIEW_PLAN = PlanUiModel(
    id = "test",
    name = "Najlepsiejszy plan treningowy".asUiText(),
    desc = "Dużo wyzwań i odpoczynku ;)\nktóż tego nie lubi".asUiText(),
    //imageUrl = Res.drawable.ic_rest_day1,
    imageUrl = Res.drawable.ic_plank1,
    days = emptyList(),
    isInProgress = true,
    difficulty = Difficulty.BEGINNER,
    themeColor = holoRed,
)

@Preview
@Composable
fun PlanHeaderPreview() {
    PlanHeader(PREVIEW_PLAN)
}
