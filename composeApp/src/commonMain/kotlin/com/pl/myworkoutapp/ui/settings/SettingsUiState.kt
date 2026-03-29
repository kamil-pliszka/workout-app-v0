package com.pl.myworkoutapp.ui.settings

import com.pl.myworkoutapp.domain.model.user.Gender
import com.pl.myworkoutapp.ui.common.UiText

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDirty: Boolean = false,
    val showCamera: Boolean = false,
    val showDeleteConfirm: Boolean = false,
    val showLanguageChooser: Boolean = false,
    //val langTag: String = "",
    val langLabel: UiText? = null,
    //val visibleRestore: Boolean = false,
    val appVersionDate: String = "",
    val appVersionHash: String = "",
    val photoPath: String? = null,
    val tmpPhotoPath: String? = null,   // robocze
    val name: String = "",
    val weightKg: String = "",
    val birthYear: String = "",
    val gender: Gender = Gender.UNSPECIFIED,
)
