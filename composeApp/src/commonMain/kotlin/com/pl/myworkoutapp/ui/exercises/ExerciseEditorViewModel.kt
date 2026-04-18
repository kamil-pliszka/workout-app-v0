package com.pl.myworkoutapp.ui.exercises

import androidx.lifecycle.*
import com.pl.myworkoutapp.AppStateHolder
import com.pl.myworkoutapp.core.currentTimeMilliseconds
import com.pl.myworkoutapp.core.exceptionToString
import com.pl.myworkoutapp.domain.StorageSupport
import com.pl.myworkoutapp.domain.WorkoutRepository
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.common.toUiConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.getString


class ExerciseEditorViewModel(
    private val repository: WorkoutRepository,
    //private val exerciseCoordinator: ExerciseEditorCoordinator,
    //private val messageCoordinator: MessageCoordinator,
    private val storageSupport: StorageSupport,
    savedStateHandle: SavedStateHandle,
    //private val appNavigator: AppNavigator,
    private val appStateHolder: AppStateHolder,//TODO - zastanowic sie czy potrzebne, moze zrbic eventami
) : ViewModel() {
    private val exerciseIdParam: String =
        savedStateHandle["exerciseId"] ?: error("exerciseId is required")
    private val _state = MutableStateFlow(
        ExerciseEditorUiState(isLoading = true)
    )
    val state: StateFlow<ExerciseEditorUiState> = _state

    private val _events = Channel<ExerciseEditorEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun sendEvent(event: ExerciseEditorEvent) {
        _events.send(event)
    }


    init {
        loadExerciseFromParam()
    }

    private var isActive = false
    fun onScreenEntered() {
        if (!isActive) {
            isActive = true
            appStateHolder.setExerciseEditorActive(true)
        }
    }

    fun onScreenExited() {
        if (isActive) {
            isActive = false
            appStateHolder.setExerciseEditorActive(false)
        }
    }

    private fun loadExerciseFromParam() {
        val exerciseId = exerciseIdParam.toExerciseIdOrNull()
        requireNotNull(exerciseId) { "Invalid exerciseId" }
        when (exerciseId) {
            ExerciseId.Custom.NEW -> prepareNewExercise()
            is ExerciseId.Custom -> loadExerciseById(exerciseId)
            is ExerciseId.BuiltIn -> prepareExeBasedOnBuiltin(exerciseId)
        }
    }

    private fun prepareExeBasedOnBuiltin(exerciseId: ExerciseId.BuiltIn) {
        viewModelScope.launch {
            println("prepareExeBasedOnBuiltin: $exerciseId")
            val exercise = repository.getExercise(exerciseId)
            println("Got exercise: $exercise")
            require(exercise is BuiltInExercise) {
                "Expected BuiltInExercise"
            }
            val configBase = exerciseId.toBuiltInExerciseId().toUiConfig()
            val exeModel = exercise.toExerciseEditorUiModel().copy(
                exerciseId = ExerciseId.Custom.NEW,
                name = getString(configBase.name),
            )
            _state.value = ExerciseEditorUiState(
                exercise = exeModel,
                initialExe = exeModel,
                isLoading = false,
            )
        }
    }

    private fun prepareNewExercise() {
        viewModelScope.launch {
            println("prepare empty exercise")
            val exeModel = ExerciseEditorUiModel(
                exerciseId = ExerciseId.Custom.NEW,
                //reszta z defaulta
            )
            _state.value = ExerciseEditorUiState(
                exercise = exeModel,
                initialExe = exeModel,
                isLoading = false,
            )
        }
    }

    private fun loadExerciseById(exerciseId: ExerciseId.Custom) {
        viewModelScope.launch {
            val exercise = repository.getExercise(exerciseId)
            println("Got exercise: $exercise")
            require(exercise is CustomExercise) {
                "Expected CustomExercise"
            }
            val exeModel = exercise.toExerciseEditorUiModel()
            _state.value = ExerciseEditorUiState(
                exercise = exeModel,
                initialExe = exeModel,
                isLoading = false,
            )
        }
    }

    fun onAction(action: ExerciseEditorAction) {
        println("Got action: $action")
        when (action) {
            ExerciseEditorAction.OnDeleteAction -> TODO()
            ExerciseEditorAction.OnDismissRequest -> {
                //tutaj trzeba sprawdzić czy wprowadzono jakieś zmiany, i jeśli tak, to dodać komunikat/ostrzeżenie
                if (_state.value.isDirty) {
                    _state.update { it.copy(showConfirmDiscard = true) }
                } else {
                    //appNavigator.closeDialog()
                    viewModelScope.launch {
                        sendEvent(ExerciseEditorEvent.Close)
                    }
                }
            }

            ExerciseEditorAction.OnSaveAction -> {
                onSaveExercise()
            }

            ExerciseEditorAction.OnScreenEntered -> onScreenEntered()
            ExerciseEditorAction.OnScreenExited -> onScreenExited()
            is ExerciseEditorAction.DescriptionChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(description = action.value),
                        touchedFields = touchedFields + ExeEditorField.DESCRIPTION
                    )
                }
            }

            is ExerciseEditorAction.EquipmentChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(equipment = action.value),
                        touchedFields = touchedFields + ExeEditorField.EQUIPMENT
                    )
                }
            }

            is ExerciseEditorAction.ExerciseTypeChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(exerciseType = action.value),
                        touchedFields = touchedFields + ExeEditorField.EXERCISE_TYPE
                    )
                }
            }

            is ExerciseEditorAction.MetChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(met = action.value),
                        touchedFields = touchedFields + ExeEditorField.MET
                    )
                }
            }

            is ExerciseEditorAction.MuscleChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(muscle = action.value),
                        touchedFields = touchedFields + ExeEditorField.MUSCLE
                    )
                }
            }

            is ExerciseEditorAction.NameChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(name = action.value),
                        touchedFields = touchedFields + ExeEditorField.NAME
                    )
                }
            }

            is ExerciseEditorAction.QuantityTypeChanged -> {
                updateAndValidate {
                    copy(
                        exercise = exercise.copy(quantityType = action.value),
                        touchedFields = touchedFields + ExeEditorField.QUANTITY_TYPE
                    )
                }
            }

            ExerciseEditorAction.RemoveImage -> TODO()
            is ExerciseEditorAction.OnImagePicked -> {
                action.path?.let {
                    updateAndValidate {
                        copy(
                            exercise = exercise.copy(imagePath = action.path, imageChanged = true),
                            touchedFields = touchedFields + ExeEditorField.IMAGE
                        )
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        println("VM onCleared: $this")
    }

    private inline fun updateAndValidate(
        crossinline block: ExerciseEditorUiState.() -> ExerciseEditorUiState
    ) {
        _state.update { current ->
            val newState = current.block()
            val updated = newState.copy(
                isDirty = newState.exercise != newState.initialExe
            )
            validate(updated)
        }
    }

    private fun validate(state: ExerciseEditorUiState): ExerciseEditorUiState {
        val errors = mutableMapOf<ExeEditorField, String>()
        if (state.exercise.name.isBlank()) {
            errors[ExeEditorField.NAME] = "Name cannot be empty"
        }

        //obraz jest wymagany
        if (state.exercise.imagePath == null && state.exercise.imageRes == null) {
            errors[ExeEditorField.IMAGE] = "Required"
        }

        if (state.exercise.quantityType == null) {
            errors[ExeEditorField.QUANTITY_TYPE] = "Required"
        }
        if (state.exercise.muscle == null) {
            errors[ExeEditorField.MUSCLE] = "Required"
        }
        if (state.exercise.exerciseType == null) {
            errors[ExeEditorField.EXERCISE_TYPE] = "Required"
        }
        if (state.exercise.equipment == null) {
            errors[ExeEditorField.EQUIPMENT] = "Required"
        }
        val metValue = state.exercise.met.toDoubleOrNull()
        //https://en.wikipedia.org/wiki/Metabolic_equivalent_of_task
        if (metValue == null || metValue <= 0.0) {
            errors[ExeEditorField.MET] = "Invalid MET"
        } else if (metValue > 30.0) {
            errors[ExeEditorField.MET] = "MET too big"
        }

        return state.copy(
            errors = errors,
            isValid = errors.isEmpty()
        )
    }


    private fun onSaveExercise() {
        val current = _state.value

        if (_state.value.isSaving) return

        val validated = validate(
            current.copy(touchedFields = ExeEditorField.entries.toSet())
        )

        _state.update { validated }

        if (!validated.isValid) return

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val imagePath = prepareFiles(validated)
                val updatedExercise = validated.exercise.copy(
                    imagePath = imagePath,
                    originalImagePath = imagePath,
                    imageChanged = false
                )
                val updated = validated.copy(
                    exercise = updatedExercise
                )
                saveExercise(updated)

                _state.update {
                    it.copy(
                        exercise = updated.exercise,
                        initialExe = updated.exercise,
                        isSaving = false,
                        isDirty = false
                    )
                }

            } catch (e: Throwable) {
                e.printStackTrace()
                println("Save failed: ${e.message}")
                _state.update { it.copy(isSaving = false) }
                sendEvent(
                    ExerciseEditorEvent.ShowError(
                        Res.string.exercise_editor_save_failed.asUiText(
                            exceptionToString(e)
                        )
                    )
                )
            }
        }
    }

    private suspend fun prepareFiles(validatedState: ExerciseEditorUiState): String? {
        //obrazek
        val finalPhoto =
            if (validatedState.exercise.imageChanged) {
                if (validatedState.exercise.imagePath == null) {
                    //usunięcie obrazka
                    validatedState.exercise.originalImagePath?.let {
                        storageSupport.delete(it)
                    }
                    null
                } else {
                    //zmiana istniejącego obrazka
                    val fname = "exe_" + currentTimeMilliseconds() + ".jpg"
                    val savedFilePath = storageSupport.copyTmpToFinal(
                        fromPath = validatedState.exercise.imagePath,
                        toFilename = fname
                    )
                    try {
                        validatedState.exercise.originalImagePath?.let {
                            storageSupport.delete(it)
                        }
                    } catch (e: Exception) {
                        // log only — nie przerywaj save
                        e.printStackTrace()
                    }
                    savedFilePath
                }
            } else {
                validatedState.exercise.originalImagePath
            }
        return finalPhoto
    }

    private suspend fun saveExercise(validatedState: ExerciseEditorUiState) {
        val customExercise = validatedState.exercise.toDomain()
        val savedExerciseId = repository.saveCustomExercise(customExercise)
        println("Saved as : $savedExerciseId")
        //messageCoordinator.success(Res.string.exercise_editor_save_success.asUiText())
        sendEvent(ExerciseEditorEvent.ShowMessage(Res.string.exercise_editor_save_success.asUiText()))
        //exerciseCoordinator.exerciseCreated(savedExerciseId)
        sendEvent(ExerciseEditorEvent.Completed(savedExerciseId, validatedState.exercise.exerciseId.isNew()))
    }

}