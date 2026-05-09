package com.pl.myworkoutapp.ui.workouts.details

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.core.Log
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.components.BaseOverlayScreen
import com.pl.myworkoutapp.ui.components.UiImageComponent
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WorkoutMetadataEditorScreen(
    state: WorkoutMetadataDraft,
    onAction: (MetadataAction) -> Unit,
) {
    BaseOverlayScreen(
        headerContent = {
            WorkoutMetadataEditorHeader(
                onClose = { onAction(MetadataAction.CancelMetadataEditor) }
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
                isNew = state.creationMode,
                onSave = { onAction(MetadataAction.SaveMetadataEditor) },
            )
        },
        maxHeight = 0.9f,
        onCancel = { onAction(MetadataAction.CancelMetadataEditor) }
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
    onAction: (MetadataAction) -> Unit,
) {
    SideEffect {
        Log.d("RECOMP", "WorkoutMetadataEditorMainContent")
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = {
                onAction(MetadataAction.UpdateMetadataName(it))
            },
            label = { Text(stringResource(Res.string.workout_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                //keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
            isError = state.displayError(WorkoutMetadataField.NAME),
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = {
                onAction(MetadataAction.UpdateMetadataDescription(it))
            },
            label = { Text(stringResource(Res.string.workout_desc)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            isError = state.displayError(WorkoutMetadataField.DESCRIPTION),
        )

        ImageSection(state, onAction)
    }
}

@Composable
fun ImageSection(
    state: WorkoutMetadataDraft,
    onAction: (MetadataAction) -> Unit,
) {
    val imagePicker = rememberImagePicker { path ->
        if (path != null) {//null zwracany gdy user anuluje interakcję
            onAction(MetadataAction.UpdateMetadataImage(path.asUiImage()))
        }
    }
    val hasError = state.displayError(WorkoutMetadataField.IMAGE)
    val borderColor = if (hasError)
        MaterialTheme.colorScheme.error
    else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            //.clip(RoundedCornerShape(12.dp))
            //.background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { imagePicker.pickImage() },
        contentAlignment = Alignment.Center
    ) {
        Row {
            //Column(modifier = Modifier.weight(1f).fillMaxSize()) {}
            WorkoutImage(
                modifier = Modifier.fillMaxHeight(),
                image = state.image,
                isError = hasError,
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {//right
                if (state.image.isLocalImage()) {
                    Spacer(Modifier.width(32.dp))
                    IconButton(onClick = { onAction(MetadataAction.RemoveMetadataImage) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_delete_forever),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(Res.string.btn_delete)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutImage(
    modifier: Modifier = Modifier,
    image: UiImage,
    isError: Boolean,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)     // wymusza kwadrat
        //.background(Color.Green)
        ,
        contentAlignment = Alignment.Center
    ) {
        UiImageComponent(
            modifier = Modifier
                .fillMaxSize()
                //.aspectRatio(1f)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = "exe image",
            image = image,
            contentScale = ContentScale.Fit,
            emptyImageContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(Res.drawable.ic_flying_witch),//TODO
                        contentDescription = "exe image",
                    )
                    Text(
                        text = stringResource(Res.string.exercise_editor_choose_image),
                        color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                }
            },
            noLocalImageContent = {
                Text(
                    text = stringResource(Res.string.exercise_editor_choose_image),
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
fun WorkoutMetadataEditorBottomButtons(
    isNew: Boolean,
    onSave: () -> Unit,
) {
    if (isNew) {
        Button(
            onClick = onSave,
            //modifier = Modifier.weight(1f),
        ) {
            Icon(painter = painterResource(Res.drawable.ic_arrow_forward), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.btn_next))
        }
    } else {
        Button(
            onClick = onSave,
            //modifier = Modifier.weight(1f),
        ) {
            Icon(painter = painterResource(Res.drawable.ic_check), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.btn_save))
        }
    }
}


@Preview
@Composable
private fun WorkoutMetadataEditorScreenPreview() {
    AppTheme {
        WorkoutMetadataEditorScreen(
            state = WorkoutMetadataDraft(
                image = Res.drawable.ic_bent_leg_twist.asUiImage(),
                name = "meta name",
                description = "meta desc",
                creationMode = true,
            ),
            onAction = { }
        )
    }
}

@Preview
@Composable
private fun WorkoutMetadataEditorScreenPreviewEmptyImage() {
    AppTheme {
        WorkoutMetadataEditorScreen(
            state = WorkoutMetadataDraft(
                image = UiImage.Empty,
                name = "meta name",
                description = "meta desc",
                creationMode = true,
                errors = mapOf(
                    WorkoutMetadataField.IMAGE to "???",
                ),
                //touchedFields = WorkoutMetadataField.entries.toSet()
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
                image = "ic_bent_leg_twist".asUiImage(),
                name = "meta name",
                description = "meta desc"
            ),
            onAction = { }
        )
    }
}

