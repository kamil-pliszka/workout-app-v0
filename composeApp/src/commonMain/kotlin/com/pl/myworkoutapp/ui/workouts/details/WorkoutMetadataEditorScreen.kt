package com.pl.myworkoutapp.ui.workouts.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.common.asUiImage
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.BaseOverlayScreen
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WorkoutMetadataEditorScreen(
    state: WorkoutMetadataDraft,
    onAction: (WorkoutEditAction) -> Unit,
) {
    BaseOverlayScreen(
        headerContent = {
            WorkoutMetadataEditorHeader(
                onClose = { onAction(WorkoutEditAction.CancelMetadataEditor) }
            )
        },
        mainContent = {
            WorkoutMetadataEditorMainContent(
                state = state,
                onAction = onAction
            )
        },
        bottomContent = {
            WorkoutMetadataEditorBottomButtons(
                onSave = { onAction(WorkoutEditAction.SaveMetadataEditor) },
            )
        },
        maxHeight = 0.9f,
        onCancel = { onAction(WorkoutEditAction.CancelMetadataEditor) }
    )
}

@Composable
private fun RowScope.WorkoutMetadataEditorHeader(
    onClose: () -> Unit
) {
    Text(
        text = stringResource(Res.string.workout_details),
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
fun WorkoutMetadataEditorMainContent(
    state: WorkoutMetadataDraft,
    onAction: (WorkoutEditAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.name.asString(),
            onValueChange = {
                onAction(WorkoutEditAction.UpdateMetadataName(it))
            },
            label = { Text(stringResource(Res.string.workout_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.description.asString(),
            onValueChange = {
                onAction(WorkoutEditAction.UpdateMetadataDescription(it))
            },
            label = { Text(stringResource(Res.string.workout_desc)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

        //TODO - obrazek
    }
}

@Composable
fun WorkoutMetadataEditorBottomButtons(
    onSave: () -> Unit,
) {
    Button(
        onClick = onSave,
        //modifier = Modifier.weight(1f),
    ) {
        Icon(painter = painterResource(Res.drawable.ic_check), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(stringResource(Res.string.btn_save))
    }
}


@Preview
@Composable
private fun WorkoutMetadataEditorScreenPreview() {
    AppTheme {
        WorkoutMetadataEditorScreen(
            state = WorkoutMetadataDraft(
                image = Res.drawable.ic_bent_leg_twist.asUiImage(),
                name = "meta name".asUiText(),
                description = "meta desc".asUiText()
            ),
            onAction = { }
        )
    }
}

@Preview(locale = "pl")
@Composable
private fun WorkoutMetadataEditorScreenPreviewPL() {
    AppTheme {
        WorkoutMetadataEditorScreen(
            state = WorkoutMetadataDraft(
                image = Res.drawable.ic_bent_leg_twist.asUiImage(),
                name = "meta name".asUiText(),
                description = "meta desc".asUiText()
            ),
            onAction = { }
        )
    }
}

