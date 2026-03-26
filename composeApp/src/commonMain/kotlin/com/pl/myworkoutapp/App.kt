package com.pl.myworkoutapp

import androidx.compose.runtime.Composable
import com.pl.myworkoutapp.ui.navigation.AppRoot
import com.pl.myworkoutapp.ui.theme.AppTheme

//@Composable
//@Preview
//fun App() {
//    val repo = org.koin.compose.koinInject<WorkoutRepository>()
//    Log.d("x", "mam repo")
//    val exercises by repo.observeExercises()
//        .collectAsState(initial = emptyList())
//    exercises.forEach {
//        println("Ex: $it")
//    }
//
//    println("Exercises: ")
//    BuiltInExerciseRegistry.getAllId().map { it.name }.sorted().forEach { println(it) }
//
//    println("Workouts: ")
//    BuiltInWorkoutRegistry.getAllId().map { it.name }.sorted().forEach { println(it) }
//
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}

@Composable
fun App() {
//    CompositionLocalProvider(
//        LocalPlatformEffects provides platformEffects
//    ) {
//        AppRoot()
//    }
    AppTheme {
        AppRoot()
    }
}