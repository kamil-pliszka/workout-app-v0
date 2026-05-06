package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.user.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface AppSettingRepository {
    //val settingsFlow: Flow<AppSettings>
    val languageFlow: Flow<String>

    val userProfileFlow: Flow<UserProfile>
    val weightFlow: Flow<Double>

    suspend fun getLanguageOnce(): String

    suspend fun updateLanguage(language: String)

    suspend fun save(userProfile: UserProfile)
    suspend fun clearAllSettings()
}