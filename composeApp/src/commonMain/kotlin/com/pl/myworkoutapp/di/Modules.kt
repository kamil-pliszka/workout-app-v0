package com.pl.myworkoutapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.database.WorkoutDatabase
import com.pl.myworkoutapp.data.repository.WorkoutRepositoryImpl
import com.pl.myworkoutapp.domain.WorkoutRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::WorkoutRepositoryImpl).bind<WorkoutRepository>()

    single {
        get<DatabaseFactory>().create()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<WorkoutDatabase>().workoutDao }
//
//    viewModelOf(::XXXViewModel)
}