package com.pl.myworkoutapp.domain.model.user

data class UserProfile(
    val name: String,
    val weightKg: Double,
    val birthYear: Int,
    val gender: Gender
)
