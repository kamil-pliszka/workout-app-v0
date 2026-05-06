package com.pl.myworkoutapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.database.WorkoutDatabase
import com.pl.myworkoutapp.data.mappers.WorkoutEntityTreeBuilder
import com.pl.myworkoutapp.data.mappers.WorkoutFlatteningMapper
import com.pl.myworkoutapp.data.prefs.DataStoreProvider
import com.pl.myworkoutapp.data.repository.AppSettingRepositoryImpl
import com.pl.myworkoutapp.data.repository.WorkoutRepositoryImpl
import com.pl.myworkoutapp.domain.*
import com.pl.myworkoutapp.domain.usecase.*
import com.pl.myworkoutapp.ui.app.AppStateHolder
import com.pl.myworkoutapp.ui.common.MessageCoordinator
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionViewModel
import com.pl.myworkoutapp.ui.exercises.*
import com.pl.myworkoutapp.ui.language.LanguageViewModel
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.plans.PlansViewModel
import com.pl.myworkoutapp.ui.reports.ReportsViewModel
import com.pl.myworkoutapp.ui.settings.SettingsViewModel
import com.pl.myworkoutapp.ui.workouts.*
import com.pl.myworkoutapp.ui.workouts.details.*
import com.pl.myworkoutapp.ui.workouts.tree.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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
    single { get<WorkoutDatabase>().exerciseDao }
    single { get<WorkoutDatabase>().workoutDao }
    singleOf(::AppSettingRepositoryImpl).bind<AppSettingRepository>()
    singleOf(::WorkoutFlatteningMapper)
    singleOf(::WorkoutEntityTreeBuilder)
    singleOf(::ExerciseEditorCoordinator)
    singleOf(::AppNavigator)
    singleOf(::MessageCoordinator)
    singleOf(::WorkoutHydrator)

    //UC
    singleOf(::GetWorkoutWithExercisesUseCase)
    singleOf(::SaveWorkoutUseCase)
    singleOf(::GetExerciseInfoUseCase)
    singleOf(::GetExerciseWithDefaultQuantityUseCase)
    singleOf(::GetMainWorkoutsUseCase)
    singleOf(::DeleteCustomWorkoutAndResolveFallbackUseCase)
    singleOf(::ValidateWorkoutUseCase)
    singleOf(::EstimateWorkoutMetricsUseCase)
    singleOf(::ResolveWorkoutExercisesUseCase)

    //jeśli delegate ma stan per ekran wtedy factoryOf, jeśli nie będzie miał stanu wtedy singleOf
    singleOf(::CircuitEditorDelegate)
    singleOf(::WorkoutEditReducer)
    singleOf(::WorkoutViewReducer)
    singleOf(::ExerciseInteractionReducer)
    singleOf(::WorkoutSessionCoordinator)
    singleOf(::WorkoutDropHandlerArchived)
    singleOf(::WorkoutDropPolicyArchived)
    singleOf(::WorkoutTreePolicy)
    singleOf(::WorkoutTreeMutator)
    singleOf(::WorkoutTreeNormalizer)
    singleOf(::WorkoutTreeMutationHandler)

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