package com.pl.myworkoutapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.core.Constants.PROFILE_PHOTO_FILENAME
import com.pl.myworkoutapp.domain.AppSettingRepository
import com.pl.myworkoutapp.domain.StorageSupport
import com.pl.myworkoutapp.domain.model.user.Gender
import com.pl.myworkoutapp.domain.model.user.UserProfile
import com.pl.myworkoutapp.ui.common.UiText
import com.pl.myworkoutapp.ui.common.asUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.lang_en
import myworkoutapplication.composeapp.generated.resources.lang_pl

class SettingsViewModel(
    private val repository: AppSettingRepository,
    private val storageSupport: StorageSupport,
) : ViewModel() {
    private val _state = MutableStateFlow(
        SettingsUiState()
    )
    val state: StateFlow<SettingsUiState> = _state

    init {
        observeLang()
        observeProfile()
    }

    private fun observeLang() {
        repository.languageFlow
            .onEach { lang ->
                println("observeLang: $lang")
                _state.update {
                    it.copy(
                        //langTag = lang,
                        langLabel = langDisplayResId(lang),
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun observeProfile() {
        repository.userProfileFlow
            .onEach { profile ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        name = profile.name.orEmpty(),
                        weightKg = profile.weightKg?.toString() ?: "",
                        gender = profile.gender ?: Gender.UNSPECIFIED,
                        photoPath = profile.photoPath,
                        tmpPhotoPath = null,
                        isDirty = false
                    )
                }
            }
            .onStart { _state.update { it.copy(isLoading = true) } }
            .launchIn(viewModelScope)
    }

    private fun langDisplayResId(tag: String): UiText? {
        return when (tag) {
            "pl" -> Res.string.lang_pl.asUiText()
            "en" -> Res.string.lang_en.asUiText()
            else -> null
        }
    }

    fun onAction(action: SettingsAction) {
        println("Got action: $action")
        when (action) {
            SettingsAction.OnDeleteAllDataClick -> {
                _state.update { it.copy(showDeleteConfirm = true) }
            }

            SettingsAction.OnDeleteAllDataCancel -> {
                _state.update { it.copy(showDeleteConfirm = false) }
            }

            SettingsAction.OnDeleteAllDataConfirm -> {
                _state.update { it.copy(showDeleteConfirm = false) }
                processDeleteAllData()
            }

            SettingsAction.OnLanguageClick -> {
                _state.update { it.copy(showLanguageChooser = true) }
            }

            SettingsAction.OnLanguageDismiss -> {
                _state.update { it.copy(showLanguageChooser = false) }
            }

            is SettingsAction.OnLanguageChoose -> {
                setLanguage(action.lang)
                _state.update { it.copy(showLanguageChooser = false) }
            }

            is SettingsAction.OnImagePicked -> {
                _state.update {
                    it.copy(
                        showCamera = false,
                        tmpPhotoPath = action.path ?: it.tmpPhotoPath,
                        isDirty = true
                    )
                }
            }

            is SettingsAction.OnPhotoCaptured -> {
                _state.update {
                    it.copy(
                        showCamera = false,
                        tmpPhotoPath = action.path ?: it.tmpPhotoPath,
                        isDirty = true
                    )
                }
            }

            SettingsAction.OnCameraStart -> {
                _state.update { it.copy(showCamera = true) }
            }

            SettingsAction.OnSaveProfile -> {
                saveProfile()
            }
        }
    }

    private fun processDeleteAllData() {
        TODO()
//        launchWithErrorHandling {
//            withLoading {
//                backupService.deleteAll()
//                sendInfo(R.string.settings_delete_all_success)
//                updateRestoreAvailability()
//            }
//        }
    }

    private fun setLanguage(tag: String) {
        println("setLanguage to: $tag")
        // Używamy Dispatchers.Main.immediate, aby AppCompatDelegate
        // mógł natychmiastowo zareagować na zmianę konfiguracji.
        viewModelScope.launch(Dispatchers.Main.immediate) {
            try {
                _state.update { it.copy(isLoading = true) }
                repository.updateLanguage(tag)
            } catch (e: Exception) {
                e.printStackTrace()
                //sendMessage(e.message ?: "Unknown error")
                throw e
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend inline fun <T> withSaving(
        crossinline block: suspend () -> T
    ): T {
        return try {
            _state.update { it.copy(isSaving = true) }
            block()
        } finally {
            _state.update { it.copy(isSaving = false) }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            try {
                withSaving {
                    val s = state.value

                    val finalPhoto = if (s.tmpPhotoPath != null) {
                        storageSupport.copyTmpToFinal(
                            fromPath = s.tmpPhotoPath,
                            toFilename = PROFILE_PHOTO_FILENAME
                        )
                    } else {
                        s.photoPath
                    }

                    val userProfile = UserProfile(
                        name = null,
                        weightKg = null,
                        birthYear = null,
                        gender = null,
                        photoPath = finalPhoto
                    )

                    repository.save(userProfile)
                    _state.update {
                        it.copy(isDirty = false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //sendMessage(e.message ?: "Unknown error")
                throw e
            }
        }
    }

}