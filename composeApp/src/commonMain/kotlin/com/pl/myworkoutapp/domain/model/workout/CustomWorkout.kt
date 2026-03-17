package com.pl.myworkoutapp.domain.model.workout

import com.pl.myworkoutapp.domain.model.Difficulty

data class CustomWorkout(
    override val id: WorkoutId.Custom,
    val name: String,
    val description: String?,
    // imageUri w domenie jest OK w tym wypadku, bo nie jest typem UI, tylko reprezentacją zasobu
    // (może być plik, content:// lub URL). To typowy kompromis w KMP
    val imageUri: String?,// np. "file://...", "content://...", lub URL do zasobu

    override val difficulty: Difficulty,
    override val estimatedDuration: Int, //in seconds
    override val exercises: List<WorkoutItem>,
) : Workout