package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.done_all
import myworkoutapplication.composeapp.generated.resources.ic_jumping_jacks
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExerciseCard(
    qty: String,
    icon: DrawableResource,
    isDone: Boolean,
    isCurrent: Boolean,
    themeColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.matchParentSize(),
        )
        // 🔹 badge (czas / powtórzenia)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(
                    Color.Gray.copy(alpha = 0.8f),
                    RoundedCornerShape(topStart = 8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = qty,
                color = Color.White
            )
        }
        //DONE flag
        if (isDone) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.done_all),
                    contentDescription = "Well done",
                    tint = themeColor,
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ExerciseCardPreview() {
    ExerciseCard(
        qty = "x 37",
        icon = Res.drawable.ic_jumping_jacks,
        isDone = true,
        isCurrent = true,
        themeColor = Color.Cyan
    )
}