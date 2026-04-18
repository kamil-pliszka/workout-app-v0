@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.pl.myworkoutapp.ui.exercises

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.core.StringComparator
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.Exercise
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.ui.common.loadExerciseDescription
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class ExercisePickerViewModel(
    private val repository: WorkoutRepository,
    appSettingRepository: AppSettingRepository,
    //exerciseCoordinator: ExerciseEditorCoordinator
) : ViewModel() {
    private val _state = MutableStateFlow(
        ExercisePickerUiState(isLoading = true)
    )
    val state: StateFlow<ExercisePickerUiState> = _state

    private val builtInExercisesFlow = flow {
        emit(repository.getBuiltinExercises().map {
            it.toSearchUi()
        })
    }
    private val customExercisesFlow = repository.observeCustomExercises().map { list ->
        list.map { it.toSearchUi() }
    }

    //    private val allExercisesRawFlow = flow {
//        emit(repository.getAllExercises())
//    }
    private val allExercisesFlow = combine(
        builtInExercisesFlow,
        customExercisesFlow,
        appSettingRepository.languageFlow
    ) { builtin, custom, lang ->
        println("LANG: $lang")
        //StringComparator uwzględnia locale wewnętrznie, tutaj zapięcie na flow lang żeby był impuls do ponownego sortowania
        val comparator = StringComparator()
        (builtin + custom).sortedWith { a, b -> comparator.compare(a.name, b.name) }
    }

    /*private val allExercisesFlow12 = appSettingRepository.languageFlow
        .distinctUntilChanged()
        .mapLatest { lang ->
            println("LANG: $lang")
            repository.getAllExercises()
                .map { it.toSearchUi() }
                .sortedBy { it.name }
        }*/
    val currentExerciseFlow = _state
        .map { state ->
            state.allExercises.find { it.exerciseId == state.currentExerciseId }
        }
        .distinctUntilChangedBy { it?.exerciseId }

    private val queryFlow = _state.map { it.query }.debounce(300).distinctUntilChanged()

    private val filterInputFlow = _state
        .map {
            Triple(
                it.allExercises,
                Triple(it.muscleGroups, it.equipments, it.exerciseTypes),
                it.currentExerciseId
            )
        }
        .distinctUntilChanged()

    private val filterFlow = combine(
        filterInputFlow,
        queryFlow
    ) { (all, filters, currentId), query ->

        val (muscles, eq, types) = filters

        all.asSequence()
            .filter { it.exerciseId != currentId }
            .filter { muscles.isEmpty() || it.muscle in muscles }
            .filter { eq.isEmpty() || it.equipment in eq }
            .filter { types.isEmpty() || it.exerciseType in types }
            .filter {
                query.isBlank() ||
                        it.name.contains(query, true) ||
                        it.searchKey.contains(query, true)
            }
            .toList()
    }

    val filteredExercises: StateFlow<List<ExercisePickerListItem>> =
        filterFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        observeExercises()
    }

    private fun observeExercises() {
        viewModelScope.launch {
            allExercisesFlow.collect { exercises ->
                println("observeExercises, exes: ${exercises.size}")
                _state.update {
                    it.copy(
                        isLoading = false,
                        allExercises = exercises
                    )
                }
            }
        }
    }


    fun initWithCurrentExerciseId(currentExerciseId: ExerciseId?) {
        println("initWithCurrentExerciseId: $currentExerciseId")
        _state.update {
            it.copy(
                currentExerciseId = currentExerciseId,
            )
        }
    }

    fun onAction(action: ExercisePickerAction) {
        println("Got action: $action")
        when (action) {
            ExercisePickerAction.ClearFilters -> {
                _state.update {
                    it.copy(
                        muscleGroups = emptyList(),
                        equipments = emptyList(),
                        exerciseTypes = emptyList(),
                        query = "",
                    )
                }
            }

            ExercisePickerAction.ExpandFilters -> {
                _state.update {
                    it.copy(showExpandedFilters = true)
                }
            }

            ExercisePickerAction.CloseFilters -> {
                _state.update {
                    it.copy(showExpandedFilters = false)
                }
            }

            is ExercisePickerAction.Search -> {
                _state.update { it.copy(query = action.q) }
            }

            is ExercisePickerAction.AddEquipmentFilter -> {
                _state.update {
                    it.copy(equipments = (it.equipments + action.equipment).distinct())
                }
            }

            is ExercisePickerAction.AddMuscleFilter -> {
                _state.update {
                    it.copy(muscleGroups = (it.muscleGroups + action.muscle).distinct())
                }
            }

            is ExercisePickerAction.AddExerciseTypeFilter -> {
                _state.update {
                    it.copy(exerciseTypes = (it.exerciseTypes + action.type).distinct())
                }
            }

            is ExercisePickerAction.SetFilters -> {
                _state.update {
                    it.copy(
                        muscleGroups = action.muscles.toList(),
                        equipments = action.equipments.toList(),
                        exerciseTypes = action.types.toList(),
                        showExpandedFilters = false,
                    )
                }
            }

            is ExercisePickerAction.RemoveMuscleFilter -> {
                _state.update {
                    it.copy(muscleGroups = it.muscleGroups.filter { m -> m != action.muscle })
                }
            }

            is ExercisePickerAction.RemoveEquipmentFilter -> {
                _state.update {
                    it.copy(equipments = it.equipments.filter { e -> e != action.equipment })
                }
            }

            is ExercisePickerAction.RemoveExerciseTypeFilter -> {
                _state.update {
                    it.copy(exerciseTypes = it.exerciseTypes.filter { e -> e != action.type })
                }
            }

            is ExercisePickerAction.ExerciseSelectionToggle -> {
                println("ExerciseSelectionToggle : ${action.exerciseId}")
                _state.update {
                    it.copy(
                        selectedExerciseId = if (it.selectedExerciseId == action.exerciseId) null else action.exerciseId
                    )
                }
            }

            is ExercisePickerAction.ExercisePreview -> {
                exercisePreview(action.exerciseId)
            }

            ExercisePickerAction.ExercisePreviewClose -> {
                _state.update {
                    it.copy(exercisePreview = null)
                }
            }
        }
    }

    private fun exercisePreview(exerciseId: ExerciseId) {
        viewModelScope.launch {
            println("Exe preview clicked: $exerciseId")
            val exercise: Exercise = repository.getExercise(exerciseId)
            val exerciseInfo = exercise.toUi()
            val exerciseMarkdown = exerciseInfo
                .takeIf { it.customDesc == null }
                ?.descExerciseId
                ?.let { id ->
                    loadExerciseDescription(
                        exerciseId = id,
                        lang = Locale.current.language
                    )
                }
            _state.update {
                it.copy(
                    exercisePreview = exerciseInfo.copy(
                        descriptionMarkdown = exerciseMarkdown
                    )
                )
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        println("VM onCleared: $this")
    }
}