package com.pl.myworkoutapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual class DataStoreProvider(
    private val context: Context
) {
    actual fun createDataStore(fileName: String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath {
            context.filesDir.resolve(fileName).absolutePath.toPath()
        }
    }
}