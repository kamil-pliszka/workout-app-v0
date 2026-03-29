package com.pl.myworkoutapp.domain

import com.pl.myworkoutapp.domain.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {
    //val settingsFlow: Flow<AppSettings>
    val languageFlow: Flow<String>

    val userProfileFlow: Flow<UserProfile>

    suspend fun getLanguageOnce(): String

    suspend fun updateLanguage(language: String)

    suspend fun save(userProfile: UserProfile)
    suspend fun clearAllSettings()
}