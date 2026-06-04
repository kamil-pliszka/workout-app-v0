package com.pl.myworkoutapp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pl.myworkoutapp.data.database.DatabaseFactory
import com.pl.myworkoutapp.data.database.WorkoutDatabase
import com.pl.myworkoutapp.data.mappers.WorkoutEntityTreeBuilder
import com.pl.myworkoutapp.data.mappers.WorkoutFlatteningMapper
import com.pl.myworkoutapp.data.prefs.DataStoreProvider
import com.pl.myworkoutapp.data.repository.AppSettingRepositoryImpl
import com.pl.myworkoutapp.data.repository.WorkoutRepositoryImpl
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.WorkoutHydrator
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.usecase.DeleteCustomWorkoutAndResolveFallbackUseCase
import com.pl.myworkoutapp.domain.usecase.EstimateWorkoutMetricsUseCase
import com.pl.myworkoutapp.domain.usecase.ExerciseCompletedUseCase
import com.pl.myworkoutapp.domain.usecase.GetExerciseInfoUseCase
import com.pl.myworkoutapp.domain.usecase.GetExerciseWithDefaultQuantityUseCase
import com.pl.myworkoutapp.domain.usecase.GetMainWorkoutsUseCase
import com.pl.myworkoutapp.domain.usecase.GetWorkoutWithExercisesUseCase
import com.pl.myworkoutapp.domain.usecase.PrepareWorkoutExecutionUseCase
import com.pl.myworkoutapp.domain.usecase.ResolveWorkoutExercisesUseCase
import com.pl.myworkoutapp.domain.usecase.SaveWorkoutUseCase
import com.pl.myworkoutapp.domain.usecase.ValidateAndEstimateWorkoutUseCase
import com.pl.myworkoutapp.domain.usecase.ValidateWorkoutUseCase
import com.pl.myworkoutapp.ui.app.AppStateHolder
import com.pl.myworkoutapp.ui.common.KeepScreenController
import com.pl.myworkoutapp.ui.common.MessageCoordinator
import com.pl.myworkoutapp.ui.execution.WorkoutExecutionViewModel
import com.pl.myworkoutapp.ui.execution.engine.ExecutionEffectHandler
import com.pl.myworkoutapp.ui.execution.engine.ExecutionEffectResolver
import com.pl.myworkoutapp.ui.execution.engine.ExecutionEventResolver
import com.pl.myworkoutapp.ui.execution.engine.ExecutionPlanBuilder
import com.pl.myworkoutapp.ui.execution.engine.ExecutionReducer
import com.pl.myworkoutapp.ui.execution.engine.ExecutionTimer
import com.pl.myworkoutapp.ui.execution.engine.WorkoutExecutionEngine
import com.pl.myworkoutapp.ui.execution.engine.ExecutionEventHandler
import com.pl.myworkoutapp.ui.exercises.ExerciseEditorCoordinator
import com.pl.myworkoutapp.ui.exercises.ExerciseEditorViewModel
import com.pl.myworkoutapp.ui.exercises.ExercisePickerViewModel
import com.pl.myworkoutapp.ui.language.LanguageViewModel
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.plans.PlansViewModel
import com.pl.myworkoutapp.ui.reports.ReportsViewModel
import com.pl.myworkoutapp.ui.settings.SettingsViewModel
import com.pl.myworkoutapp.ui.workouts.WorkoutDropHandlerArchived
import com.pl.myworkoutapp.ui.workouts.WorkoutDropPolicyArchived
import com.pl.myworkoutapp.ui.workouts.WorkoutsViewModel
import com.pl.myworkoutapp.ui.workouts.details.CircuitEditorDelegate
import com.pl.myworkoutapp.ui.workouts.details.ExerciseInteractionReducer
import com.pl.myworkoutapp.ui.workouts.details.WorkoutDetailsViewModel
import com.pl.myworkoutapp.ui.workouts.details.WorkoutEditReducer
import com.pl.myworkoutapp.ui.workouts.details.WorkoutMetadataEditorReducer
import com.pl.myworkoutapp.ui.workouts.details.WorkoutSessionCoordinator
import com.pl.myworkoutapp.ui.workouts.details.WorkoutViewReducer
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeMutationHandler
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeMutator
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreeNormalizer
import com.pl.myworkoutapp.ui.workouts.tree.WorkoutTreePolicy
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
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
    singleOf(::KeepScreenController)

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
    singleOf(::ValidateAndEstimateWorkoutUseCase)
    singleOf(::PrepareWorkoutExecutionUseCase)
    singleOf(::ExerciseCompletedUseCase)

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
    singleOf(::WorkoutMetadataEditorReducer)
    //exec
    factoryOf(::WorkoutExecutionEngine)//engine ma stan
    singleOf(::ExecutionTimer)
    singleOf(::ExecutionReducer)
    singleOf(::ExecutionPlanBuilder)
    singleOf(::ExecutionEffectResolver)
    singleOf(::ExecutionEventResolver)
    singleOf(::ExecutionEffectHandler)
    singleOf(::ExecutionEventHandler)

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