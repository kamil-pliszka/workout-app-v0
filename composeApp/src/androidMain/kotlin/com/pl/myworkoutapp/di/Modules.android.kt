package com.pl.myworkoutapp.di

import android.app.Activity
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.ui.effects.AndroidPlatformEffects
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
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
    }