@file:OptIn(ExperimentalMaterial3Api::class)

package com.pl.myworkoutapp.ui.workouts.details

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.SegmentedButtonDefaults.itemShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.BaseOverlayScreen
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.workouts.CircuitStructureType
import com.pl.myworkoutapp.ui.workouts.asUiText
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

//OVERLAY SCREEN
@Composable
fun CircuitEditorScreen(
    state: CircuitEditorUiState,
    onEditorAction: (CircuitEditorAction) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    // Backdrop / Scrim
    // brzydko, ale jednolicie z pozostałymi ekranami
    BaseOverlayScreen(
        headerContent = {
            CircuitEditorHeader(
                isNew = state.isNew,
                onClose = onCancel
            )
        },
        mainContent = {
            CircuitEditorContent(
                modifier = Modifier,
                state = state,
                onEditorAction = onEditorAction,
            )
        },
        bottomContent = {
            CircuitEditorBottomButtons(
                onSave = onSave,
                saveEnabled = state.isValid
            )
        },
        maxHeight = 0.99f,
        onCancel = onCancel
    )
}

@Composable
private fun RowScope.CircuitEditorHeader(
    isNew: Boolean,
    onClose: () -> Unit
) {
    Text(
        text = if (isNew) stringResource(Res.string.circuit_label_new)
        else stringResource(Res.string.circuit_label_edit),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    IconButton(onClick = onClose) {
        Icon(
            painter = painterResource(Res.drawable.ic_close),
            contentDescription = stringResource(Res.string.btn_close)
        )
    }
}

@Composable
fun CircuitEditorBottomButtons(
    onSave: () -> Unit,
    saveEnabled: Boolean
) {
    Button(
        onClick = onSave,
        //modifier = Modifier.weight(1f),
        enabled = saveEnabled
    ) {
        Icon(painter = painterResource(Res.drawable.ic_check), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(stringResource(Res.string.btn_save))
    }
}


@Composable
fun CircuitEditorScreenScaffold(
    state: CircuitEditorUiState,
    onEditorAction: (CircuitEditorAction) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.isNew) Text(stringResource(Res.string.circuit_label_new))
                    else Text(stringResource(Res.string.circuit_label_edit))
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = stringResource(Res.string.btn_back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSave,
                        // UX: If not valid, you might want to keep it enabled
                        // and show a Snackbar/Toast explaining what's missing,
                        // but keeping it disabled is also a valid M3 pattern.
                        enabled = state.isValid
                    ) {
                        Text(stringResource(Res.string.btn_save))
                    }
                }
            )
        }
    ) { padding ->
        CircuitEditorContent(
            modifier = Modifier.padding(padding),
            state = state,
            onEditorAction = onEditorAction,
        )
    }
}

@Composable
fun CircuitEditorContent(
    modifier: Modifier = Modifier,
    state: CircuitEditorUiState,
    onEditorAction: (CircuitEditorAction) -> Unit,
) {
    // Add FocusRequesters for better navigation
    val (nameFocus, firstAttrFocus) = remember { FocusRequester.createRefs() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // -------- PHASE --------
        SectionHeader(stringResource(Res.string.circuit_phase))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            Phase.entries.forEachIndexed { index, phase ->
                SegmentedButton(
                    selected = state.phase == phase,
                    onClick = { onEditorAction(CircuitEditorAction.PhaseChanged(phase)) },
                    shape = itemShape(index, Phase.entries.size)
                ) {
                    Text(phase.asUiText().asString())
                }
            }
        }

        // -------- NAME --------
        OutlinedTextField(
            value = state.name.asString(),
            onValueChange = { onEditorAction(CircuitEditorAction.NameChanged(it)) },
            label = { Text(stringResource(Res.string.circuit_name_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { firstAttrFocus.requestFocus() })
        )

        // -------- STRUCTURE --------
        SectionHeader(stringResource(Res.string.circuit_structure))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            CircuitStructureType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = state.structureType == type,
                    onClick = { onEditorAction(CircuitEditorAction.StructureChanged(type)) },
                    shape = itemShape(index, CircuitStructureType.entries.size)
                ) {
                    Text(type.asUiText().asString())
                }
            }
        }

        // -------- STRUCTURE DETAILS --------
        AnimatedContent(
            targetState = state.structureType,
            label = "StructureFieldsTransition",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { structureType ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                when (structureType) {
                    CircuitStructureType.Standard -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                32.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.RoundsChanged("2"))
                                },
                                label = { Text("2") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.RoundsChanged("3"))
                                },
                                label = { Text("3") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.RoundsChanged("4"))
                                },
                                label = { Text("4") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.RoundsChanged("5"))
                                },
                                label = { Text("5") }
                            )
                        }

                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_rounds),
                            value = state.rounds,
                            onChange = { onEditorAction(CircuitEditorAction.RoundsChanged(it)) },
                            imeAction = ImeAction.Done,
                            modifier = Modifier.focusRequester(firstAttrFocus)
                        )
                    }

                    CircuitStructureType.EMOM -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                32.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.EmomMinutesChanged("10"))
                                },
                                label = { Text("10") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.EmomMinutesChanged("12"))
                                },
                                label = { Text("12") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.EmomMinutesChanged("15"))
                                },
                                label = { Text("15") }
                            )
                        }
                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_emom_minutes),
                            value = state.emomMinutes,
                            onChange = { onEditorAction(CircuitEditorAction.EmomMinutesChanged(it)) },
                            imeAction = ImeAction.Done,
                            modifier = Modifier.focusRequester(firstAttrFocus)
                        )
                    }

                    CircuitStructureType.AMRAP -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                32.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.AmrapMinutesChanged("10"))
                                },
                                label = { Text("10") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.AmrapMinutesChanged("12"))
                                },
                                label = { Text("12") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.AmrapMinutesChanged("15"))
                                },
                                label = { Text("15") }
                            )
                        }
                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_amrap_minutes),
                            value = state.amrapMinutes,
                            onChange = { onEditorAction(CircuitEditorAction.AmrapMinutesChanged(it)) },
                            imeAction = ImeAction.Done,
                            modifier = Modifier.focusRequester(firstAttrFocus)
                        )
                    }

                    CircuitStructureType.Tabata -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                32.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.TabataRoundsChanged("8"))
                                    onEditorAction(CircuitEditorAction.TabataWorkSecChanged("20"))
                                    onEditorAction(CircuitEditorAction.TabataRestSecChanged("10"))
                                },
                                label = { Text("8/20/10") }
                            )
                            AssistChip(
                                onClick = {
                                    onEditorAction(CircuitEditorAction.TabataRoundsChanged("6"))
                                    onEditorAction(CircuitEditorAction.TabataWorkSecChanged("30"))
                                    onEditorAction(CircuitEditorAction.TabataRestSecChanged("15"))
                                },
                                label = { Text("6/30/15") }
                            )
                        }
                        val restFocus = remember { FocusRequester() }
                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_tabata_rounds),
                            value = state.tabataRounds,
                            onChange = { onEditorAction(CircuitEditorAction.TabataRoundsChanged(it)) },
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { restFocus.requestFocus() }),
                            modifier = Modifier.focusRequester(firstAttrFocus)
                        )
                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_tabata_worksec),
                            value = state.tabataWorkSec,
                            onChange = { onEditorAction(CircuitEditorAction.TabataWorkSecChanged(it)) },
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { restFocus.requestFocus() }),
                            modifier = Modifier.focusRequester(firstAttrFocus)
                        )
                        CircuitNumberField(
                            label = stringResource(Res.string.circuit_tabata_restsec),
                            value = state.tabataRestSec,
                            onChange = { onEditorAction(CircuitEditorAction.TabataRestSecChanged(it)) },
                            imeAction = ImeAction.Done,
                            modifier = Modifier.focusRequester(restFocus)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall, // Use titleSmall for better hierarchy
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun CircuitNumberField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    // Improved error logic: show error if empty OR non-positive
    val intValue = value.toIntOrNull()
    val isError = value.isNotEmpty() && (intValue == null || intValue <= 0)

    OutlinedTextField(
        value = value,
        onValueChange = { new ->
            if (new.isEmpty() || new.all { it.isDigit() }) {
                onChange(new)
            }
        },
        label = { Text(label) },
        isError = isError,
        supportingText = if (isError) {
            { Text(stringResource(Res.string.circuit_error_numer_must_be_greater_zero)) }
        } else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}


// ---------- PREVIEW ----------
@Preview(locale = "pl")
@Composable
private fun CircuitEditorScreenStandardPrevew() {
    AppTheme {
        CircuitEditorScreen(
            state = CircuitEditorUiState(
                isNew = true,
                phase = Phase.MAIN,
                name = "circ name".asUiText(),
                structureType = CircuitStructureType.Standard,
                rounds = "6",
                emomMinutes = "",
                amrapMinutes = "",
                tabataWorkSec = "",
                tabataRestSec = ""
            ),
            onEditorAction = { },
            onSave = { },
            onCancel = { },
        )
    }
}

@Preview
@Composable
private fun CircuitEditorScreenEMOMPrevew() {
    AppTheme {
        CircuitEditorScreen(
            state = CircuitEditorUiState(
                isNew = true,
                phase = Phase.WARMUP,
                name = "circ name".asUiText(),
                structureType = CircuitStructureType.EMOM,
                rounds = "",
                emomMinutes = "13",
                amrapMinutes = "",
                tabataWorkSec = "",
                tabataRestSec = ""
            ),
            onEditorAction = { },
            onSave = { },
            onCancel = { },
        )
    }
}

@Preview
@Composable
private fun CircuitEditorScreenAMRAPPrevew() {
    AppTheme {
        CircuitEditorScreen(
            state = CircuitEditorUiState(
                isNew = false,
                phase = Phase.WARMUP,
                name = "circ name".asUiText(),
                structureType = CircuitStructureType.AMRAP,
                rounds = "",
                emomMinutes = "",
                amrapMinutes = "71",
                tabataWorkSec = "",
                tabataRestSec = ""
            ),
            onEditorAction = { },
            onSave = { },
            onCancel = { },
        )
    }
}

@Preview
@Composable
private fun CircuitEditorScreenTabataPrevew() {
    AppTheme {
        CircuitEditorScreen(
            state = CircuitEditorUiState(
                isNew = false,
                phase = Phase.WARMUP,
                name = "circ name".asUiText(),
                structureType = CircuitStructureType.Tabata,
                rounds = "",
                emomMinutes = "",
                amrapMinutes = "",
                tabataWorkSec = "17",
                tabataRestSec = "13"
            ),
            onEditorAction = { },
            onSave = { },
            onCancel = { },
        )
    }
}