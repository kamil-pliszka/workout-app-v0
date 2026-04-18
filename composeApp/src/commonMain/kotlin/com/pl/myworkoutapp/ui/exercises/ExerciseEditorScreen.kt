package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.*

@Composable
fun ExerciseEditorScreen(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    LaunchedEffect(Unit) {
        onAction(ExerciseEditorAction.OnScreenEntered)
    }
    DisposableEffect(Unit) {
        onDispose {
            onAction(ExerciseEditorAction.OnScreenExited)
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onAction(ExerciseEditorAction.OnDismissRequest)
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.99f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* consume */ }
        ) {
            Spacer(Modifier.height(8.dp))
            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.exercise.exerciseId == ExerciseId.Custom.NEW)
                        stringResource(Res.string.exercise_editor_add_exercise)
                    else
                        stringResource(Res.string.exercise_editor_edit_exercise),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
                /*TextButton(onClick = { onAction(ExerciseEditorAction.OnDismissRequest) } ) {
                    Text(stringResource(Res.string.btn_close))
                }*/
                IconButton(onClick = { onAction(ExerciseEditorAction.OnDismissRequest) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "close"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .imePadding()
                    .fillMaxWidth()
                    .weight(1f)
                    //.height(IntrinsicSize.Min)
                    .verticalScroll(rememberScrollState())
            ) {
                //Spacer(Modifier.height(16.dp))
                //CONTENT
                ExerciseEditorContent(
                    state = state,
                    onAction = onAction
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 32.dp,
                    alignment = Alignment.CenterHorizontally
                ),
            ) {
                /*Button(
                    onClick = { onAction(ExerciseEditorAction.OnDismissRequest)},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.btn_close))
                }*/
                /*Button(onClick = { onAction(ExerciseEditorAction.OnDismissRequest) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        //tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.btn_cancel)
                    )
                    Text(stringResource(Res.string.btn_cancel))
                }*/
                if (false) {//kiedyś dodamy warunek
                    Button(
                        onClick = { onAction(ExerciseEditorAction.OnDeleteAction) },
                        colors = buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete_forever),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(Res.string.btn_delete)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(stringResource(Res.string.btn_delete))
                    }
                }
                Button(
                    onClick = { onAction(ExerciseEditorAction.OnSaveAction) },
                    //enabled = state.isValid
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        //tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.btn_save)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(Res.string.btn_save))
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditorContent(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicInfoSection(state, onAction)
        ImageSection(state, onAction)
        //BasedOnSection(state, onAction) - na razie do pominięcia
        ParametersSection(state, onAction)
    }
}


@Composable
fun BasicInfoSection(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = state.exercise.name,
            onValueChange = { onAction(ExerciseEditorAction.NameChanged(it)) },
            label = { Text(stringResource(Res.string.exercise_editor_name)) },
            isError = state.displayError(ExeEditorField.NAME),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
        )

        OutlinedTextField(
            value = state.exercise.description,
            onValueChange = { onAction(ExerciseEditorAction.DescriptionChanged(it)) },
            label = { Text(stringResource(Res.string.exercise_editor_desc)) },
            isError = state.displayError(ExeEditorField.DESCRIPTION),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            supportingText = {
                if (state.exercise.basedOn != null) {
                    Text(stringResource(Res.string.exercise_editor_desc_info))
                }
            }
        )
    }
}


@Composable
fun ImageSection(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    val imagePicker = rememberImagePicker { path ->
        onAction(ExerciseEditorAction.OnImagePicked(path))
    }
    val hasError = state.displayError(ExeEditorField.IMAGE)
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
            ExerciseImage(
                modifier = Modifier.fillMaxHeight(),
                imageRes = state.exercise.imageRes,
                imagePath = state.exercise.imagePath,
                isError = hasError,
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {//right
                if (state.exercise.imagePath != null) {
                    Spacer(Modifier.width(32.dp))
                    IconButton(onClick = { onAction(ExerciseEditorAction.RemoveImage) }) {
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
fun ExerciseImage(
    modifier: Modifier = Modifier,
    imageRes: DrawableResource?,
    imagePath: String?,
    isError: Boolean,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)     // wymusza kwadrat
        //.background(Color.Green)
        ,
        contentAlignment = Alignment.Center
    ) {
        when {
            imagePath != null -> {
                val photoBitmap = remember(imagePath) {
                    loadImageBitmap(imagePath)
                }
                if (photoBitmap != null) {
                    Image(
                        bitmap = photoBitmap,
                        contentDescription = "exe image",
                        modifier = Modifier
                            .fillMaxSize()
                            //.aspectRatio(1f)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.exercise_editor_choose_image),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            imageRes != null -> {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "exe image",
                    modifier = Modifier
                        .fillMaxSize()
                        //.aspectRatio(1f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                Column(
                    //modifier = Modifier.ali
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(Res.drawable.ic_exercise),
                        contentDescription = "exe image",
                    )
                    Text(
                        text = stringResource(Res.string.exercise_editor_choose_image),
                        color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
                    )
                }
            }
        }
    }
}


@Composable
fun BasedOnSection(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text(
            text = "Ćwiczenie bazowe",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedButton(
            //TODO - raczej zbedne, nie bedzie możliwości edycji tego
            onClick = { /*onAction(ExerciseEditorAction.SelectBasedOn)*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = state.exercise.basedOn?.let { "Wybrane: $it" }
                    ?: "Wybierz ćwiczenie bazowe"
            )
        }

        if (state.exercise.basedOn != null) {

            Text(
                text = "Dziedziczy tłumaczenia i zasoby z ćwiczenia bazowego",
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                //TODO - raczej zbedne, nie bedzie możliwości edycji tego
                onClick = { /*onAction(Action.ClearBasedOn)*/ }
            ) {
                Text("Usuń powiązanie")
            }
        }
    }
}

@Composable
fun ParametersSection(
    state: ExerciseEditorUiState,
    onAction: (ExerciseEditorAction) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        /*Text(
            text = stringResource(Res.string.exercise_editor_params),
            style = MaterialTheme.typography.titleMedium
        )*/

        val quantityTypeValues = remember { QuantityType.entries.toList() }
        EnumDropdown(
            label = stringResource(Res.string.exercise_editor_qty_type),
            value = state.exercise.quantityType,
            values = quantityTypeValues,
            text = { it.asUiText() },
            image = { it.getImageResource() },
            onSelected = { onAction(ExerciseEditorAction.QuantityTypeChanged(it)) },
            isError = state.displayError(ExeEditorField.QUANTITY_TYPE),
        )

        val muscleValues = remember { MuscleGroup.entries.toList() }
        EnumDropdown(
            label = stringResource(Res.string.exercise_editor_muscle_groups),
            value = state.exercise.muscle,
            values = muscleValues,
            text = { it.asUiText() },
            image = { it.getImageResource() },
            onSelected = { onAction(ExerciseEditorAction.MuscleChanged(it)) },
            isError = state.displayError(ExeEditorField.MUSCLE),
        )

        val exeTypesValues = remember { ExerciseType.entries.toList() }
        EnumDropdown(
            label = stringResource(Res.string.exercise_editor_exe_type),
            value = state.exercise.exerciseType,
            values = exeTypesValues,
            text = { it.asUiText() },
            image = { it.getImageResource() },
            onSelected = { onAction(ExerciseEditorAction.ExerciseTypeChanged(it)) },
            isError = state.displayError(ExeEditorField.EXERCISE_TYPE),
        )

        val equipmentValues = remember { Equipment.entries.toList() }
        EnumDropdown(
            label = stringResource(Res.string.exercise_editor_equipment),
            value = state.exercise.equipment,
            values = equipmentValues,
            text = { it.asUiText() },
            image = { it.getImageResource() },
            onSelected = { onAction(ExerciseEditorAction.EquipmentChanged(it)) },
            isError = state.displayError(ExeEditorField.EQUIPMENT),
        )

        Column {
            OutlinedTextField(
                value = state.exercise.met,
                onValueChange = { onAction(ExerciseEditorAction.MetChanged(it)) },
                label = { Text(stringResource(Res.string.exercise_editor_met)) },
                isError = state.displayError(ExeEditorField.MET),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = stringResource(Res.string.exercise_editor_met_info),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> EnumDropdown(
    label: String,
    value: T?,
    values: List<T>,
    text: (T) -> UiText,
    image: (T) -> DrawableResource,
    onSelected: (T) -> Unit,
    isError: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value?.let { text(it).asString() } ?: "",
            placeholder = { Text(stringResource(Res.string.exercise_editor_choose_value)) },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = {
                value?.let {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(image(value)),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            isError = isError
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach { item ->
                val isSelected = item == value
                val label = text(item).asString()
                DropdownMenuItem(
                    text = {
                        Text(label)
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(image(item)),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check),
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

@Preview
@Composable
private fun ExerciseEditorPreview() {
    AppTheme {
        ExerciseEditorScreen(
            state = ExerciseEditorUiState(
                exercise = ExerciseEditorUiModel(
                    exerciseId = ExerciseId.Custom.NEW,
                    name = "nazwa ćwiczenia",
                    //description = "tu będzie opis ćwiczenia",
                    imagePath = "url obrazka",
                    basedOn = BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
                    muscle = null, //MuscleGroup.BACK,
                    exerciseType = ExerciseType.STRENGTH,
                    equipment = Equipment.KETTLEBELL,
                    met = "1.23",
                    quantityType = QuantityType.REPS,
                ),
                errors = mapOf(
                    ExeEditorField.MET to "???",
                    ExeEditorField.MUSCLE to "!",
                    ExeEditorField.IMAGE to "",
                ),
                touchedFields = ExeEditorField.entries.toSet()
            ),
            onAction = { }
        )
    }
}

@Preview
@Composable
private fun ExerciseImagePreview() {
    AppTheme {
        Row {
            ExerciseImage(
                modifier = Modifier.size(120.dp),
                imageRes = null,
                imagePath = null,
                isError = true,
            )
            ExerciseImage(
                modifier = Modifier.size(120.dp),
                imageRes = Res.drawable.ic_flying_witch1,
                imagePath = null,
                isError = true,
            )
            ExerciseImage(
                modifier = Modifier.size(120.dp),
                imageRes = Res.drawable.ic_flying_witch1,
                imagePath = "/path/",
                isError = true,
            )
        }
    }
}
