package com.pl.myworkoutapp.ui.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.workouts.components.WorkoutEditableItemCircuit
import com.pl.myworkoutapp.ui.workouts.components.WorkoutEditableItemExercise
import kotlinx.coroutines.launch
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


/**
 * ekran załączany do WorkoutDetailsScreen, operujący na części view modelu (editableWorkout)
 * powstał w celu umożliwienia edycji obiektu, który może być zmodyfikowany(a jeszcze nie zapisany)
 * w ramach ekranu WorkoutDetailsScreen, więc modyfikujemy ten sam WorkoutDetailsUiState przez VM
 */
@Composable
fun WorkoutEditorScreen(
    state: WorkoutWithExercisesUiModel,
    onAction: (WorkoutDetailsAction) -> Unit,
    onEditorAction: (WorkoutEditorAction) -> Unit,
) {
    // Backdrop / Scrim
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onAction(WorkoutDetailsAction.OnCloseEditor)
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.99f) // Slightly less than 1.0 to show it's an overlay
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .imePadding() // Ensures UI moves up when keyboard appears
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* Consume clicks to prevent closing */ }
        ) {
            WorkoutEditorHeader(
                title = state.workout.workoutId.asString(),
                onClose = { onAction(WorkoutDetailsAction.OnCloseEditor) }
            )

            // Content area
            Box(modifier = Modifier.weight(1f)) {
                WorkoutEditorContent(
                    state = state,
                    onAction = onAction,
                    onEditorAction = onEditorAction
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            WorkoutEditorBottomButtons(
                onReset = { onAction(WorkoutDetailsAction.OnResetEditor) },
                onSave = { onAction(WorkoutDetailsAction.OnSaveEditor) },
                onAddExercise = { onAction(WorkoutDetailsAction.ShowExercisePicker) },
                onAddCircuit = { onAction(WorkoutDetailsAction.OnAddCircuit) },
            )
        }
    }
}

@Composable
private fun WorkoutEditorHeader(
    title: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
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
}

@Composable
fun WorkoutEditorBottomButtons(
    onReset: () -> Unit,
    onSave: () -> Unit,
    onAddExercise: () -> Unit, // Zmienione: konkretna akcja
    onAddCircuit: () -> Unit,  // Zmienione: konkretna akcja
    saveEnabled: Boolean = true
) {
    // Stan menu
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
            ) {
                Icon(painter = painterResource(Res.drawable.ic_reset_settings), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.btn_reset))
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = saveEnabled
            ) {
                Icon(painter = painterResource(Res.drawable.ic_check), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.btn_save))
            }
        }

        // FAB z Menu
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-56).dp)
        ) {
            FloatingActionButton(
                onClick = { menuExpanded = true },
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.btn_add)
                )
            }

            // Menu wyboru
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.workout_add_exercise)) },
                    leadingIcon = { Icon(painterResource(Res.drawable.ic_exercise), contentDescription = null) },
                    onClick = {
                        menuExpanded = false
                        onAddExercise()
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.workout_add_circuit)) },
                    leadingIcon = { Icon(painterResource(Res.drawable.ic_cycle), contentDescription = null) },
                    onClick = {
                        menuExpanded = false
                        onAddCircuit()
                    }
                )
            }
        }
    }
}

@Composable
fun WorkoutEditorContent(
    state: WorkoutWithExercisesUiModel,
    onAction: (WorkoutDetailsAction) -> Unit,
    onEditorAction: (WorkoutEditorAction) -> Unit,

) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val dragDropState = rememberDragDropState(
        listState = listState,
        onMove = { from, to ->
            onEditorAction(WorkoutEditorAction.OnMove(from, to))
            if (to == 0 || from == 0) {
                scope.launch { listState.animateScrollToItem(0) }
            }
        }
    )
    LaunchedEffect(state.scrollToIdx) {
        state.scrollToIdx?.let {
            listState.animateScrollToItem(state.scrollToIdx)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        itemsIndexed(
            items = state.items,
            key = { _, item -> item.key }
        ) { index, item ->
            DraggableItem(dragDropState = dragDropState, index = index) {
                WorkoutEditableItemRow(
                    item = item,
                    themeColor = state.workout.themeColor,
                    dragDropState = dragDropState,
                    index = index,
                    isDragging = dragDropState.draggingIndex == index,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun WorkoutEditableItemRow(
    item: WorkoutUiItem,
    themeColor: Color,
    dragDropState: DragDropState,
    index: Int,
    isDragging: Boolean,
    onAction: (WorkoutDetailsAction) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                if (isDragging) {
                    scaleX = 1.03f
                    scaleY = 1.03f
                    shadowElevation = 8.dp.toPx()
                }
            }
            .background(if (isDragging) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .pointerInput(item.key, index) {
                    detectDragGestures(
                        onDragStart = { dragDropState.onDragStart(index) },
                        onDragEnd = { dragDropState.onDragEnd() },
                        onDragCancel = { dragDropState.onDragCancel() },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragDropState.onDrag(dragAmount.y)
                        }
                    )
                }
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_drag_handle),
                contentDescription = stringResource(Res.string.btn_drag_handle),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Item Content
        Box(modifier = Modifier.weight(1f)) {
            when (item) {
                is CircuitUiItem -> WorkoutEditableItemCircuit(
                    item,
                    themeColor,
                    onClick = {},
                    onEditClick = { onAction(WorkoutDetailsAction.OnEditCircuit(item)) },
                    onDeleteClick = { onAction(WorkoutDetailsAction.OnDeleteCircuit(item)) }
                )
                is ExerciseUiItem -> WorkoutEditableItemExercise(item, themeColor, onClick = {})
            }
        }
    }
}



////////////////////////////////////////////////////////////////////////////////////////////////////

@Preview(locale = "pl")
@Composable
private fun WorkoutEditorScreenPreviewNoSet() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET)
    val workoutUiModel = transform(workout)
    AppTheme {
        WorkoutEditorScreen(
            state = workoutUiModel,
            onAction = { },
            onEditorAction = { },
        )
    }
}

@Preview(locale = "en")
@Composable
private fun WorkoutEditorScreenPreviewWithSet() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    val workoutUiModel = transform(workout)
    AppTheme {
        WorkoutEditorScreen(
            state = workoutUiModel,
            onAction = { },
            onEditorAction = { },
        )
    }
}
