package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.pl.myworkoutapp.domain.model.workout.WorkoutId
import com.pl.myworkoutapp.ui.navigation.ScreenRoutes
import com.pl.myworkoutapp.ui.workouts.components.WorkoutCard

@Composable
fun WorkoutsScreen(
    state: WorkoutsUiState,
    onAction: (WorkoutsAction) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
        Text("Workouts")
    }

    if (state.isLoading) {
        CircularProgressIndicator()
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.workouts.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        onAction(WorkoutsAction.OnPageChanged(pagerState.currentPage))
    }

    HorizontalPager(state = pagerState) { page ->
        val workout = state.workouts[page]

        WorkoutCard(
            workout = workout,
            onClick = { workoutUiItem ->//TODO
                println("workoutUiItem = $workoutUiItem")
                //onAction(WorkoutsAction.OnStartPlan(workout.id))
            }
        )
    }


}

//Przejście do wykonywania treningu
//TODO
fun startWorkout(navController: NavController, workoutId: WorkoutId) {
    navController.navigate(ScreenRoutes.WorkoutExecution.create(workoutId.toString()))
}