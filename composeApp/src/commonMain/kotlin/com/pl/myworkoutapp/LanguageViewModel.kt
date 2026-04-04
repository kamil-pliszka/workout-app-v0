package com.pl.myworkoutapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.myworkoutapp.core.getSystemLanguage
import com.pl.myworkoutapp.domain.AppSettingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

//wstrzykiwać za pomocą koinInject
class LanguageViewModel(
    settingsRepo: AppSettingRepository,
) : ViewModel() {

    val language = settingsRepo.languageFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            getSystemLanguage()
        )

}