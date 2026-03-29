package com.pl.myworkoutapp.di

import com.pl.myworkoutapp.data.IosStorageSupport
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.prefs.DataStoreProvider
import com.pl.myworkoutapp.domain.StorageSupport
import com.pl.myworkoutapp.ui.effects.IOSPlatformEffects
import com.pl.myworkoutapp.ui.effects.PlatformEffects
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
//        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
        single<PlatformEffects> { IOSPlatformEffects() }
        single { DataStoreProvider() }
        singleOf(::IosStorageSupport).bind<StorageSupport>()
    }