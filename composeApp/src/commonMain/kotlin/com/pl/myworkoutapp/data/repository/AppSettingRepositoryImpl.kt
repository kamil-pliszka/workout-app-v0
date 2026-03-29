package com.pl.myworkoutapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pl.myworkoutapp.core.getSystemLanguage
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.model.user.Gender
import com.pl.myworkoutapp.domain.model.user.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppSettingRepositoryImpl(
    //private val dataStoreProvider: DataStoreProvider
    private val dataStore: DataStore<Preferences>
) : AppSettingRepository {

    companion object {
        private val LANG_KEY = stringPreferencesKey("app_language")
        private val PHOTO_KEY = stringPreferencesKey("user_photo_path")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val WEIGHT_KEY = floatPreferencesKey("user_weight")
        private val BIRTH_YEAR_KEY = intPreferencesKey("user_birth_year")
        private val GENDER_KEY = stringPreferencesKey("user_gender")
    }

    override val languageFlow: Flow<String> =
        dataStore.data
            .map { it[LANG_KEY] ?: getSystemLanguage() }
            .distinctUntilChanged()

    override val userProfileFlow: Flow<UserProfile> =
        dataStore.data.map { prefs ->
            UserProfile(
                name = prefs[NAME_KEY],
                weightKg = prefs[WEIGHT_KEY],
                birthYear = prefs[BIRTH_YEAR_KEY],
                gender = Gender.entries.firstOrNull { it.name == prefs[GENDER_KEY] },
                photoPath = prefs[PHOTO_KEY]
            )
        }.distinctUntilChanged() //UserProfile jest data class

    //override suspend fun getLanguageOnce(): String = languageFlow.first()
    override suspend fun getLanguageOnce(): String =
        dataStore.data
            .map { it[LANG_KEY] ?: getSystemLanguage() }
            .first()

    override suspend fun updateLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[LANG_KEY] = language
        }
    }

    private fun <T> MutablePreferences.setOrRemove(
        key: Preferences.Key<T>,
        value: T?
    ) {
        if (value != null) this[key] = value else remove(key)
    }

    override suspend fun save(userProfile: UserProfile) {
        dataStore.edit { prefs ->
            prefs.setOrRemove(PHOTO_KEY, userProfile.photoPath)
            prefs.setOrRemove(NAME_KEY, userProfile.name)
            prefs.setOrRemove(WEIGHT_KEY, userProfile.weightKg)
            prefs.setOrRemove(BIRTH_YEAR_KEY, userProfile.birthYear)
            prefs.setOrRemove(GENDER_KEY, userProfile.gender?.name)
        }
    }

    //usuwa WSZYSTKIE klucze
    override suspend fun clearAllSettings() {
        dataStore.edit { prefs ->
            prefs.clear() // usuwa wszystkie klucze zapisane w DataStore
        }
    }

}
