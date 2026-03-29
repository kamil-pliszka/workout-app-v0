package com.pl.myworkoutapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pl.myworkoutapp.core.ContentBytes
import com.pl.myworkoutapp.ui.common.CameraScreenContent
import com.pl.myworkoutapp.ui.common.ConfirmationDialog
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.common.rememberImagePicker
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.choose_lang
import myworkoutapplication.composeapp.generated.resources.ic_gallery_thumbnail
import myworkoutapplication.composeapp.generated.resources.ic_outline_add_a_photo
import myworkoutapplication.composeapp.generated.resources.lang_en
import myworkoutapplication.composeapp.generated.resources.lang_pl
import myworkoutapplication.composeapp.generated.resources.profile_pick_from_gallery
import myworkoutapplication.composeapp.generated.resources.profile_save
import myworkoutapplication.composeapp.generated.resources.profile_take_a_photo
import myworkoutapplication.composeapp.generated.resources.settings_backup_and_recovery
import myworkoutapplication.composeapp.generated.resources.settings_delete_all_data
import myworkoutapplication.composeapp.generated.resources.settings_delete_all_data_delete_button
import myworkoutapplication.composeapp.generated.resources.settings_delete_all_data_description
import myworkoutapplication.composeapp.generated.resources.settings_delete_all_data_question
import myworkoutapplication.composeapp.generated.resources.settings_general
import myworkoutapplication.composeapp.generated.resources.settings_language
import myworkoutapplication.composeapp.generated.resources.settings_profile
import myworkoutapplication.composeapp.generated.resources.settings_reset
import myworkoutapplication.composeapp.generated.resources.settings_version
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val imagePicker = rememberImagePicker { path ->
        onAction(SettingsAction.OnImagePicked(path))
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally
        ) {
            ListItem(
                headlineContent = { Text(stringResource(Res.string.settings_profile)) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )

            //kolumna z obrazkiem
            Column(
                modifier = Modifier
                    //.weight(1f)
                    //.fillMaxHeight()
                    .fillMaxWidth(0.3f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                ProfilePhotoComponent(
                    photoPath = state.photoPath,
                    tmpPhotoPath = state.tmpPhotoPath
                )
                //przyciski
                Row {
                    Button(
                        onClick = { imagePicker.pickImage() },
                        contentPadding = PaddingValues(start = 4.dp),
                    ) {
                        //Text("Wybierz z galerii")
                        Icon(
                            painter = painterResource(Res.drawable.ic_gallery_thumbnail),
                            contentDescription = stringResource(Res.string.profile_pick_from_gallery),
                            modifier = Modifier.size(28.dp), // rozmiar samej ikonki
                            //tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Button(
                        onClick = { onAction(SettingsAction.OnCameraStart) },
                        contentPadding = PaddingValues(start = 4.dp)
                    ) {
                        //Text("Zrób zdjęcie")
                        Icon(
                            painter = painterResource(Res.drawable.ic_outline_add_a_photo), // tu możesz wrzucić inną ikonę aparatu
                            contentDescription = stringResource(Res.string.profile_take_a_photo),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            ) {
                Button(
                    onClick = {
                        onAction(SettingsAction.OnSaveProfile)
                    },
                    enabled = state.isDirty && !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(Res.string.profile_save))
                    }
                }
            }

            ListItem(
                headlineContent = { Text(stringResource(Res.string.settings_general)) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
            DataRow(Modifier.clickable {
                onAction(SettingsAction.OnLanguageClick)
            }) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.settings_language)
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = state.langLabel?.asString() ?: "–"
                )
            }

            ListItem(
                headlineContent = { Text(stringResource(Res.string.settings_backup_and_recovery)) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )


            ListItem(
                headlineContent = { Text(stringResource(Res.string.settings_reset)) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )

            DataRow(Modifier.clickable {
                onAction(SettingsAction.OnDeleteAllDataClick)
            }) {
                Text(
                    stringResource(Res.string.settings_delete_all_data),
                    color = MaterialTheme.colorScheme.error
                )
            }

            ListItem(
                headlineContent = {
                    Row {
                        Text(stringResource(Res.string.settings_version))
                        Text(" : ")
                        Text("${state.appVersionDate}-${state.appVersionHash}")
                    }
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
            DataRow {
                Text("")//pusty na koniec, dla FAB
            }
        }
    }

    if (state.showDeleteConfirm) {
        ConfirmationDialog(
            title = stringResource(Res.string.settings_delete_all_data_question),
            text = stringResource(Res.string.settings_delete_all_data_description),
            onConfirm = {
                onAction(SettingsAction.OnDeleteAllDataConfirm)
            },
            confirmText = stringResource(Res.string.settings_delete_all_data_delete_button),
            confirmColor = MaterialTheme.colorScheme.error,
            onCancel = {
                onAction(SettingsAction.OnDeleteAllDataCancel)
            },
        )
    }

    if (state.showLanguageChooser) {
        ChooseLangComponent(
            modifier = Modifier,
            onDismissRequest = { onAction(SettingsAction.OnLanguageDismiss) },
            onChooseLang = { onAction(SettingsAction.OnLanguageChoose(it)) }
        )
    }

    // 🔴 OVERLAY KAMERY
    if (state.showCamera) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // półprzezroczyste tło jak dialog
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f))
                // blokuje kliknięcia pod spodem
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // kliknięcie w tło zamyka kamerę (opcjonalnie)
                    onAction(SettingsAction.OnPhotoCaptured(null))
                }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f)
            ) {
                CameraScreenContent(
                    onResult = { result ->
                        onAction(SettingsAction.OnPhotoCaptured(result))
                    },
                )
            }
        }
    }
}


@Composable
private inline fun DataRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseLangComponent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onChooseLang: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.choose_lang),
                style = MaterialTheme.typography.titleLarge
            )

            LanguageItem(stringResource(Res.string.lang_pl)) {
                onChooseLang("pl")
            }
            LanguageItem(stringResource(Res.string.lang_en)) {
                onChooseLang("en")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun LanguageItem(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
    }
}


@Preview(locale = "pl")
@Composable
fun SettingsScreenPreviewPL() {
    AppTheme {
        SettingsScreen(
            SettingsUiState(
                langLabel = "PL".asUiText(),
                appVersionDate = "2026-03-27",
                appVersionHash = "43433f"
            ),
            onAction = {}
        )
    }
}

@Preview(locale = "en")
@Composable
fun SettingsScreenPreviewEN() {
    AppTheme {
        SettingsScreen(
            SettingsUiState(
                langLabel = "EN".asUiText(),
                appVersionDate = "2026-03-27",
                appVersionHash = "43433f"
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreviewDeleteConfirm() {
    AppTheme {
        SettingsScreen(
            SettingsUiState(
                showDeleteConfirm = true,
                langLabel = "EN".asUiText(),
                appVersionDate = "2026-03-27",
                appVersionHash = "43433f"
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreviewLanguageChooser() {
    AppTheme {
        SettingsScreen(
            SettingsUiState(
                showLanguageChooser = true,
                langLabel = "EN".asUiText(),
                appVersionDate = "2026-03-27",
                appVersionHash = "43433f"
            ),
            onAction = {}
        )
    }
}

