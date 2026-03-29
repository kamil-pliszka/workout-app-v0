package com.pl.myworkoutapp.di

import android.app.Activity
import com.pl.myworkoutapp.data.AndroidStorageSupport
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.prefs.DataStoreProvider
import com.pl.myworkoutapp.domain.StorageSupport
import com.pl.myworkoutapp.ui.effects.AndroidPlatformEffects
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
//        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
        single<PlatformEffects> {
            AndroidPlatformEffects {
                // dostajemy aktualne Activity z Composable lub Activity
                (androidContext() as? Activity)
            }
        }
        single { DataStoreProvider(androidApplication()) }
        singleOf(::AndroidStorageSupport).bind<StorageSupport>()
    }