package com.pl.myworkoutapp

import com.pl.myworkoutapp.domain.model.exercise.BuiltInExerciseId
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


fun main123() {
    println("Hello World!")
    val dir = "C:\\work\\prj\\szkolenia\\szkolenie-kotlin-2024\\MyWorkoutApplication\\composeApp\\src\\commonMain\\composeResources\\files\\exercises\\"
    val langs = listOf("pl", "en")
    BuiltInExerciseId.entries.forEach {exeId ->
        val idfname = exeId.name.lowercase()
        langs.forEach { lang ->
            val fname = "$idfname.$lang.md"
            val file = File(dir + fname)
            if (!file.exists()) {
                println("Create: $fname")
                file.writeText("""
Opis ćwiczenia: ${exeId.name} 
wersja: $lang

## Wskazówki
- Nie odrywaj dolnej części pleców

## Błędy
- Zbyt szybkie tempo
                """.trimIndent())
            }
        }
    }
}

fun main() {
    val dir = "C:\\work\\prj\\szkolenia\\szkolenie-kotlin-2024\\MyWorkoutApplication\\composeApp\\src\\commonMain\\composeResources\\files\\exercises\\"
    val langs = listOf("pl", "en")
    BuiltInExerciseId.entries.forEach {exeId ->
        val name = exeId.name.lowercase()
        val dirx = File(dir + name)
        dirx.mkdirs()
        langs.forEach { lang ->
            val oldfname = "$name.$lang.md"
            val oldfile = File(dir + oldfname)
            if (oldfile.exists()) {
                val newname = "$lang.md"
                val sourcePath = Paths.get(oldfile.path)
                val targetPath = Paths.get(dirx.path + "/" + newname)
                println("moving: $oldfname -> $targetPath")
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}