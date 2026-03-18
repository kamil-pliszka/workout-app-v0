package com.pl.myworkoutapp.di

import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.ui.effects.IOSPlatformEffects
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
//        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
        single<PlatformEffects> { IOSPlatformEffects() }
    }