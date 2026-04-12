package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.Difficulty
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.components.DifficultyBadge
import com.pl.myworkoutapp.ui.theme.EurostileExt
import com.pl.myworkoutapp.ui.workouts.WorkoutUiModel
import com.pl.myworkoutapp.ui.workouts.toUi
import org.jetbrains.compose.resources.painterResource


@Composable
fun WorkoutHeaderCard(
    workout: WorkoutUiModel,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.background(
                Brush.horizontalGradient(
                    listOf(
                        workout.themeColor.copy(alpha = 0.7f),
                        workout.themeColor,
                    )
                )
            )
        ) {
            // Tło (obraz)
            Image(
                painter = painterResource(workout.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )

            // Gradient overlay (czytelność tekstu)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f),
                                Color.Transparent,
                            )
                        )
                    )
            )

            // Treść
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = workout.name.asString(),
                    color = Color.White,
                    //fontSize = 22.sp,
                    //fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = EurostileExt
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(workout.durationText.asString(), color = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text(workout.kcalText.asString(), color = Color.White)
                }
            }

            // Badge (np. NORMAL / EASY)
            /*Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(Color.Yellow, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = workout.difficulty.name,
                    fontWeight = FontWeight.Bold
                )
            }*/
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                DifficultyBadge(workout.difficulty)
            }


            // Ikona serca (top-right)
            /*
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
            */
        }
    }
}

@Preview(locale = "pl")
@Composable
fun WorkoutHeaderCardPreview() {
    val workoutUiModel = CustomWorkout(
        id = 13L.asWorkoutId(),
        name = "BICEPS AND TRICEPS",
        description = "",
        imageUri = null,
        basedOn = BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET.asWorkoutId(),
        difficulty = Difficulty.ADVANCED,
        items = emptyList()
    ).toUi()
    WorkoutHeaderCard(
        workout = workoutUiModel,
        onClick = {}
    )
}