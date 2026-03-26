package com.pl.myworkoutapp.ui.common

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import com.pl.myworkoutapp.domain.model.plan.BuiltInTrainingPlanId
import com.pl.myworkoutapp.domain.model.workout.BuiltInWorkoutId
import myworkoutapplication.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.MissingResourceException

private val ExeCache = mutableMapOf<String, String?>()
suspend fun loadExerciseDescription(
    exerciseId: BuiltInExerciseId,
    lang: String
): String? {
    //val lang = getSystemLanguage()
    val resId = exerciseId.name.lowercase()
    val key = "$resId-$lang"
    return ExeCache.getOrPut(key) {
        loadDescription("exercises", resId, lang)
    }
    //return loadDescription("exercises", resId, lang, fallbackLang)
}

suspend fun loadWorkoutDescription(
    workoutId: BuiltInWorkoutId,
    lang: String,
): String? {
    val resId = workoutId.name.lowercase()
    return loadDescription("workouts", resId, lang)
}

suspend fun loadTrainingPlanDescription(
    trainingPlanId: BuiltInTrainingPlanId,
    lang: String,
): String? {
    val resId = trainingPlanId.name.lowercase()
    return loadDescription("plans", resId, lang)
}

private suspend fun loadDescription(
    typeDir: String,
    resId: String,
    lang: String,
    fallbackLang: String = "en"
): String? {
    val primaryPath = "files/$typeDir/$resId/$lang.md"

    // 1. spróbuj język docelowy
    readOrNull(primaryPath)?.let { return it }

    // 2. fallback do en (ale tylko jeśli to nie był już en)
    if (lang != fallbackLang) {
        val fallbackPath = "files/$typeDir/$resId/$fallbackLang.md"
        readOrNull(fallbackPath)?.let { return it }
    }

    // 3. brak czegokolwiek
    return null
}

@OptIn(InternalResourceApi::class)
private suspend fun readOrNull(path: String): String? {
    return try {
        //readResourceBytes(path).decodeToString()
        println("Loading: $path")
        Res.readBytes(path).decodeToString()
    } catch (_: MissingResourceException) {
        println("NOT FOUND : $path")
        null
    }
}