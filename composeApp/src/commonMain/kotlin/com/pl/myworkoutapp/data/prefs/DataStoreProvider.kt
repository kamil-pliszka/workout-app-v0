package com.pl.myworkoutapp.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreProvider {
    fun createDataStore(fileName: String = "prefs.preferences_pb"): DataStore<Preferences>
}