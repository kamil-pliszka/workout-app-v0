package com.pl.myworkoutapp.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DataStoreProvider {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(fileName: String): DataStore<Preferences> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(documentDirectory)
        val path = (documentDirectory.path + "/" + fileName).toPath()

        return PreferenceDataStoreFactory.createWithPath {
            path
        }
    }
}