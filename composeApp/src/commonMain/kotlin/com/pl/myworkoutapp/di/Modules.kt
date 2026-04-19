package com.pl.myworkoutapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.LanguageViewModel
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.database.WorkoutDatabase
import com.pl.myworkoutapp.data.mappers.WorkoutFlatteningMapper
import com.pl.myworkoutapp.data.mappers.WorkoutTreeBuilder
import com.pl.myworkoutapp.data.prefs.DataStoreProvider
import com.pl.myworkoutapp.data.repository.AppSettingRepositoryImpl
import com.pl.myworkoutapp.data.repository.WorkoutRepositoryImpl
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.usecase.GetWorkoutWithExercisesUseCase
import com.pl.myworkoutapp.domain.usecase.SaveWorkoutUseCase
import com.pl.myworkoutapp.ui.common.MessageCoordinator
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionViewModel
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.plans.PlansViewModel
import com.pl.myworkoutapp.ui.reports.ReportsViewModel
import com.pl.myworkoutapp.ui.settings.SettingsViewModel
import com.pl.myworkoutapp.ui.workouts.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.*
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::AppStateHolder)
    singleOf(::WorkoutRepositoryImpl).bind<WorkoutRepository>()

    single {
        get<DatabaseFactory>().create()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single {
        get<DataStoreProvider>().createDataStore()
    }
    single { get<WorkoutDatabase>().workoutDao }
    singleOf(::AppSettingRepositoryImpl).bind<AppSettingRepository>()
    singleOf(::WorkoutFlatteningMapper)
    singleOf(::WorkoutTreeBuilder)
    singleOf(::ExerciseEditorCoordinator)
    singleOf(::AppNavigator)
    singleOf(::MessageCoordinator)

    //UC
    singleOf(::GetWorkoutWithExercisesUseCase)
    singleOf(::SaveWorkoutUseCase)

    //jeśli delegate ma stan per ekran wtedy factory, jeśli nie będzie miał stanu wtedy single
    //na razie nie wiem, zostawiam stanowy
    factoryOf( :: WorkoutEditorDelegate)
    factoryOf( :: CircuitEditorDelegate)

    //VM
    viewModelOf(::LanguageViewModel)
    viewModelOf(::PlansViewModel)
    viewModelOf(::ReportsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::WorkoutExecutionViewModel)
    viewModelOf(::WorkoutsViewModel)
    viewModelOf(::WorkoutDetailsViewModel)
    viewModelOf(::ExercisePickerViewModel)
    viewModelOf(::ExerciseEditorViewModel)
}