package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.*


@Composable
fun ExercisePickerFilters(
    muscleGroups: List<MuscleGroup>,
    equipments: List<Equipment>,
    exerciseTypes: List<ExerciseType>,
    onClose: () -> Unit,
    onSaveFilters: (List<MuscleGroup>, List<Equipment>, List<ExerciseType>) -> Unit,
) {
    val selectedMuscles = remember { muscleGroups.toMutableStateList() }
    val selectedEquipment = remember { equipments.toMutableStateList() }
    val selectedExeTypes = remember { exerciseTypes.toMutableStateList() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { /* consume click */ }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.89f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        ) {
            Spacer(Modifier.height(8.dp))
            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.exercise_filters_label),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
                )
                TextButton(onClick = onClose) {
                    Text(stringResource(Res.string.btn_close))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    //.height(IntrinsicSize.Min)
                    .verticalScroll(rememberScrollState())
            ) {
                //Spacer(Modifier.height(16.dp))
                //CONTENT
                ExercisePickerFiltersContent(
                    muscleGroups = MuscleGroup.entries.toList(),
                    equipments = Equipment.entries.toList(),
                    exerciseTypes = ExerciseType.entries.toList(),
                    selectedMuscles = selectedMuscles,
                    selectedEquipment = selectedEquipment,
                    selectedExeTypes = selectedExeTypes,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onClose,
                    modifier = Modifier.weight(1f),
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                ) {
                    Text(stringResource(Res.string.btn_cancel))
                }
                Button(
                    onClick = {
                        onSaveFilters(selectedMuscles, selectedEquipment, selectedExeTypes)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(Res.string.btn_save))
                }
            }
        }
    }
}

@Composable
private fun ExercisePickerFiltersContent(
    muscleGroups: List<MuscleGroup>,
    equipments: List<Equipment>,
    exerciseTypes: List<ExerciseType>,
    selectedMuscles: MutableList<MuscleGroup>,
    selectedEquipment: MutableList<Equipment>,
    selectedExeTypes: MutableList<ExerciseType>,
) {
    Column {
        Text(
            text = stringResource(Res.string.exercise_filters_muscle_groups, selectedMuscles.size),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        FilterGrid(
            items = muscleGroups,
            selectedItems = selectedMuscles,
            label = { it.asUiText().asString() },
            image = { it.getImageResource() },
            onToggle = { item ->
                if (selectedMuscles.contains(item)) {
                    selectedMuscles.remove(item)
                } else {
                    selectedMuscles.add(item)
                }
            }
        )

        //Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.exercise_filters_equipment, selectedEquipment.size),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        FilterGrid(
            items = equipments,
            selectedItems = selectedEquipment,
            label = { it.asUiText().asString() },
            image = { it.getImageResource() },
            onToggle = { item ->
                if (selectedEquipment.contains(item)) {
                    selectedEquipment.remove(item)
                } else {
                    selectedEquipment.add(item)
                }
            }
        )

        Text(
            text = stringResource(Res.string.exercise_filters_exe_type, selectedExeTypes.size),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        FilterGrid(
            items = exerciseTypes,
            selectedItems = selectedExeTypes,
            label = { it.asUiText().asString() },
            image = { it.getImageResource() },
            onToggle = { item ->
                if (selectedExeTypes.contains(item)) {
                    selectedExeTypes.remove(item)
                } else {
                    selectedExeTypes.add(item)
                }
            }
        )

    }
}


@Composable
fun <T> FilterGrid(
    items: List<T>,
    selectedItems: List<T>,
    label: @Composable (T) -> String,
    image: (T) -> DrawableResource,
    onToggle: (T) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { item ->
            FilterCard(
                text = label(item),
                image = image(item),
                selected = selectedItems.contains(item),
                onClick = { onToggle(item) }
            )
        }
        /*items.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEach { item ->
                    FilterCard(
                        text = label(item),
                        image = image(item),
                        selected = selectedItems.contains(item),
                        onClick = { onToggle(item) }
                    )
                }
            }
        }*/
    }
}


@Composable
fun FilterCard(
    text: String,
    image: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else Color.LightGray.copy(alpha = 0.2f)

    val borderColor =
        if (selected) MaterialTheme.colorScheme.primary
        else Color.Transparent

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                //.background(Color.Gray, RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "icon",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = selected,
            onCheckedChange = { onClick() }
        )
    }
}


@Preview(locale = "pl")
@Composable
private fun ExercisePickerFiltersPreviewPL() {
    AppTheme {
        ExercisePickerFilters(
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.ARMS),
            equipments = listOf(
                Equipment.BODYWEIGHT,
                Equipment.BARBELL,
                Equipment.RINGS,
                Equipment.ROPE
            ),
            exerciseTypes = listOf( ExerciseType.STRETCH, ExerciseType.MOBILITY ),
            onClose = { },
            onSaveFilters = { _, _, _ -> },
        )
    }

}

@Preview
@Composable
private fun ExercisePickerFiltersPreviewEN() {
    AppTheme {
        ExercisePickerFilters(
            muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.ARMS),
            equipments = listOf(
                Equipment.BODYWEIGHT,
                Equipment.BARBELL,
                Equipment.RINGS,
                Equipment.ROPE
            ),
            exerciseTypes = listOf( ExerciseType.STRETCH, ExerciseType.MOBILITY ),
            onClose = { },
            onSaveFilters = { _, _, _ -> },
        )
    }

}


