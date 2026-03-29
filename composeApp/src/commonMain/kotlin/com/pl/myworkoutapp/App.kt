package com.pl.myworkoutapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pl.myworkoutapp.ui.common.LocalAppLocale
import com.pl.myworkoutapp.ui.navigation.AppRoot
import com.pl.myworkoutapp.ui.theme.AppTheme
import org.koin.compose.koinInject

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
    AppTheme {
        val vm = koinInject<AppViewModel>()
        val language by vm.language.collectAsState()
        CompositionLocalProvider(LocalAppLocale provides language) {
            AppRoot()
        }
    }
}