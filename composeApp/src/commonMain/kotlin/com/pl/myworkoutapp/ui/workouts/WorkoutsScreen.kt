package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.navigation.ScreenRoutes

@Composable
fun WorkoutsScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
        Text("Workouts")
    }
}

//Przejście do wykonywania treningu
//TODO
fun startWorkout(navController: NavController, workoutId: WorkoutId) {
    navController.navigate(ScreenRoutes.WorkoutExecution.create(workoutId.toString()))
}