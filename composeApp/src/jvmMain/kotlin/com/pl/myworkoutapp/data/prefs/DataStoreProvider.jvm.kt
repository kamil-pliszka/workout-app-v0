package com.pl.myworkoutapp.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.pl.myworkoutapp.data.getAppDataDir
import okio.Path.Companion.toPath
import java.io.File

actual class DataStoreProvider {
    actual fun createDataStore(fileName: String): DataStore<Preferences> {
        val appDataDir = getAppDataDir()
        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val filePath = File(appDataDir, fileName).absolutePath.toPath()

        return PreferenceDataStoreFactory.createWithPath {
            filePath
        }
    }
}