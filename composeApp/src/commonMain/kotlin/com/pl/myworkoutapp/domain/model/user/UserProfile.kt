package com.pl.myworkoutapp.domain.model.user

data class UserProfile(
    val name: String?,
    val weightKg: Float?,
    val birthYear: Int?,
    val gender: Gender?,
    val photoPath: String?, // ścieżka do pliku
)
