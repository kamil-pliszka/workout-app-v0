package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
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
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
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
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                    RoundedCornerShape(topStart = 8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = qty,
                color = MaterialTheme.colorScheme.onPrimary
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
                    painter = painterResource(Res.drawable.ic_done_all),
                    contentDescription = "Well done",
                    tint = themeColor,
                    modifier = Modifier.scale(0.7f)
                )
            }
        }
    }
}


@Composable
fun ExerciseCardEditable(
    name: String,
    qty: String,
    icon: DrawableResource,
    isDone: Boolean,
    themeColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            //fontWeight = FontWeight.Bold,
        )
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
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                    RoundedCornerShape(topStart = 8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = qty,
                color = MaterialTheme.colorScheme.onPrimary
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
                    painter = painterResource(Res.drawable.ic_done_all),
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
    AppTheme {
        ExerciseCard(
            qty = "x 37",
            icon = Res.drawable.ic_jumping_jacks,
            isDone = true,
            isCurrent = true,
            themeColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
fun ExerciseCardEditablePreview() {
    AppTheme {
        ExerciseCardEditable(
            name = "Nazwa ćwiczenia",
            qty = "x 31",
            icon = Res.drawable.ic_flying_witch1,
            isDone = true,
            themeColor = MaterialTheme.colorScheme.secondary
        )
    }
}