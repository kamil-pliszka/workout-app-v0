package com.pl.myworkoutapp.ui.settings

import com.pl.myworkoutapp.core.ContentBytes

sealed interface SettingsAction {
    object OnLanguageClick : SettingsAction
    object OnDeleteAllDataClick : SettingsAction
    object OnDeleteAllDataCancel : SettingsAction
    object OnDeleteAllDataConfirm : SettingsAction
    object OnLanguageDismiss : SettingsAction
    data class OnLanguageChoose(val lang: String) : SettingsAction

    data class OnImagePicked(val path: String?) : SettingsAction
    data class OnPhotoCaptured(val path: String?) : SettingsAction
    object OnCameraStart : SettingsAction
    object OnSaveProfile : SettingsAction
//    data class NavToWorkout(val workoutId: String) : WorkoutsAction
}