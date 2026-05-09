package com.pl.myworkoutapp.ui.workouts.details

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.ExerciseId
import com.pl.myworkoutapp.domain.model.workout.*
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.components.BaseOverlayScreen
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.theme.EurostileExt
import com.pl.myworkoutapp.ui.workouts.*
import com.pl.myworkoutapp.ui.workouts.components.*
import com.pl.myworkoutapp.ui.workouts.tree.transform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


/**
 * ekran załączany do WorkoutDetailsScreen, operujący na części view modelu (editableWorkout)
 * powstał w celu umożliwienia edycji obiektu, który może być zmodyfikowany(a jeszcze nie zapisany)
 * w ramach ekranu WorkoutDetailsScreen, więc modyfikujemy ten sam WorkoutDetailsUiState przez VM
 */
//OVERLAY SCREEN
@Composable
fun WorkoutEditorScreen(
    state: WorkoutEditSession,
    events: Flow<WorkoutEditorEvent>,
    onEditAction: (WorkoutEditAction) -> Unit,
    onCircuitEditorAction: (CircuitEditorAction) -> Unit,
) {
    // Backdrop / Scrim
    BaseOverlayScreen(
        headerContent = {
            WorkoutEditorHeader(
                title = state.workout.workout.name.asString(),
                onEditMetadata = { onEditAction(MetadataAction.OpenMetadataEditor.toWorkoutEditAction()) },
                onClose = { onEditAction(WorkoutEditAction.CloseEditor) }
            )
        },
        mainContent = {
            WorkoutEditorContent(
                state = state,
                events = events,
                onEditorAction = onEditAction,
                onExchangeAction = { key, exerciseId ->
                    onEditAction(WorkoutEditAction.ExerciseExchangeStart(key, exerciseId))
                },
                onExerciseClick = { key, exerciseId ->
                    onEditAction(WorkoutEditAction.ShowExerciseInfo(key, exerciseId))
                }
            )
        },
        bottomContent = {
            WorkoutEditorBottomButtons(
                onReset = { onEditAction(WorkoutEditAction.ResetDraft) },
                onSave = { onEditAction(WorkoutEditAction.SaveDraft) },
                onAddExercise = { onEditAction(WorkoutEditAction.AddExercise) },
                onAddCircuit = { onEditAction(WorkoutEditAction.AddCircuit) },
            )
        },
        maxHeight = 0.99f,
        onCancel = { onEditAction(WorkoutEditAction.CloseEditor) }
    )

    if (state.editableCircuit != null) {
        CircuitEditorScreen(
            state = state.editableCircuit,
            onEditorAction = onCircuitEditorAction,
            onSave = { onEditAction(WorkoutEditAction.SaveCircuitEditor) },
            onCancel = { onEditAction(WorkoutEditAction.CancelCircuitEditor) },
        )
    }

    if (state.editableMetadata != null) {
        WorkoutMetadataEditorScreen(
            state = state.editableMetadata,
            onAction = { action -> onEditAction(action.toWorkoutEditAction()) },
        )
    }

    if (state.modal is WorkoutEditModal.ConfirmDeleteItem) {
        ConfirmationDialog(
            title = stringResource(Res.string.workout_editor_delete_title),
            text = stringResource(Res.string.workout_editor_delete_question),
            onConfirm = {
                onEditAction(WorkoutEditAction.DeleteElementConfirm(state.modal.key))
            },
            confirmText = stringResource(Res.string.btn_delete),
            confirmButtonColors = buttonColors(MaterialTheme.colorScheme.error),
            onCancel = {
                onEditAction(WorkoutEditAction.DeleteElementCancel)
            },
        )
    }

    if (state.modal is WorkoutEditModal.ExercisePicker && state.activeExercise != null) {
        WorkoutExerciseInfoScreen(
            exerciseInfo = state.activeExercise.draft,
            showExchangeButton = false,
            onAction = { onEditAction(it.toWorkoutEditAction()) }
        )
    }

}

@Composable
private fun RowScope.WorkoutEditorHeader(
    title: String,
    onEditMetadata: () -> Unit,
    onClose: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontFamily = EurostileExt,
        modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    IconButton(onClick = onEditMetadata) {
        Icon(
            painter = painterResource(Res.drawable.ic_edit),
            contentDescription = stringResource(Res.string.btn_edit)
        )
    }
    IconButton(onClick = onClose) {
        Icon(
            painter = painterResource(Res.drawable.ic_close),
            contentDescription = stringResource(Res.string.btn_close)
        )
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

    Box(
        modifier = Modifier.offset(y = (4).dp)//nie wiem czemu
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f),
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_reset_settings),
                    contentDescription = null
                )
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
                    leadingIcon = {
                        Icon(
                            painterResource(Res.drawable.ic_exercise),
                            contentDescription = "add exe"
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onAddExercise()
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.workout_add_circuit)) },
                    leadingIcon = {
                        Icon(
                            painterResource(Res.drawable.ic_cycle),
                            contentDescription = "add circuit"
                        )
                    },
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
    state: WorkoutEditSession,
    events: Flow<WorkoutEditorEvent>,
    onEditorAction: (WorkoutEditAction) -> Unit,
    onExchangeAction: (Int, ExerciseId) -> Unit,
    onExerciseClick: (Int, ExerciseId) -> Unit,
) {
    val listState = rememberLazyListState()
    //val scope = rememberCoroutineScope()

    val dragDropState = rememberDragDropState(
        listState = listState,
        itemsProvider = { state.workout.items },
        onDrop = { event ->
            onEditorAction(WorkoutEditAction.Drop(event))
        },
    )
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is WorkoutEditorEvent.ScrollEditorTo -> {
                    listState.animateScrollToItem(event.index, -500)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        itemsIndexed(
            items = state.workout.items,
            key = { _, item -> item.key }
        ) { index, item ->
            DraggableItem(dragDropState = dragDropState, index = index) {
                WorkoutEditableItemRow(
                    item = item,
                    themeColor = state.workout.workout.themeColor,
                    dragDropState = dragDropState,
                    index = index,
                    isDragging = dragDropState.draggingIndex == index,
                    onEditorAction = onEditorAction,
                    onExchangeAction = onExchangeAction,
                    onExerciseClick = onExerciseClick,
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
    onEditorAction: (WorkoutEditAction) -> Unit,
    onExchangeAction: (Int, ExerciseId) -> Unit,
    onExerciseClick: (Int, ExerciseId) -> Unit,
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
                    onEditClick = { onEditorAction(WorkoutEditAction.EditCircuit(item.key)) },
                    onDeleteClick = { onEditorAction(WorkoutEditAction.DeleteItem(item.key)) }
                )

                is ExerciseUiItem -> WorkoutEditableItemExercise(
                    item,
                    themeColor,
                    onClick = { onExerciseClick(item.key, item.exerciseId) },
                    onExchangeClick = { onExchangeAction(item.key, item.exerciseId) },
                    onDeleteClick = { onEditorAction(WorkoutEditAction.DeleteItem(item.key)) },
                    quantityChangeAction = { increase ->
                        onEditorAction(
                            WorkoutEditAction.ChangeQuantityOnList(
                                item.key,
                                increase
                            )
                        )
                    },
                )
            }
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////

@Preview(locale = "pl")
@Composable
private fun WorkoutEditorScreenPreviewNoSet() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_WITH_SET)
    val workoutUiModel = transform(workout, WorkoutMetrics(567))
    AppTheme {
        WorkoutEditorScreen(
            state = WorkoutEditSession(
                original = workoutUiModel,
                workout = workoutUiModel.copy(),
            ),
            events = emptyFlow(),
            onEditAction = { },
            onCircuitEditorAction = { },
        )
    }
}

@Preview(locale = "en")
@Composable
private fun WorkoutEditorScreenPreviewWithSet() {
    val workout = BuiltInWorkoutRegistry.get(BuiltInWorkoutId.MY_ABS_WORKOUT_NO_SET)
    val workoutUiModel = transform(workout, WorkoutMetrics(567))
    AppTheme {
        WorkoutEditorScreen(
            state = WorkoutEditSession(
                original = workoutUiModel,
                workout = workoutUiModel.copy(),
            ),
            events = emptyFlow(),
            onEditAction = { },
            onCircuitEditorAction = { },
        )
    }
}
