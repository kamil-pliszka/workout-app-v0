package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pl.myworkoutapp.ui.common.asString
import com.pl.myworkoutapp.ui.workouts.WorkoutUiModel

@Composable
fun WorkoutHeader(
    modifier: Modifier = Modifier,
    workout: WorkoutUiModel,
) {
    Text("WORKOUT: ${workout.workoutId.asString()}")

}