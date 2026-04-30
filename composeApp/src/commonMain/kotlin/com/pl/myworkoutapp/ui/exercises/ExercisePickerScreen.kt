package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.navigation.AppNavigator
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

//wybieraczka ćwiczenia
//OVERLAY SCREEN
@Composable
fun ExercisePickerScreen(
    currentExerciseId: ExerciseId?,
    onResult: (ExerciseId?) -> Unit,
    appNavigator: AppNavigator = koinInject(),//TODO - raczej do pozbycia się
) {
    val viewModel = koinViewModel<ExercisePickerViewModel>()
    /*LaunchedEffect(Unit) {
        appStateHolder.setHideNavigation(true)
    }
    DisposableEffect(Unit) {
        onDispose {
            appStateHolder.setHideNavigation(false)
        }
    }*/
    //println("ExercisePickerScreen: $currentExerciseId")
    val filtered by viewModel.filteredExercises.collectAsStateWithLifecycle()
    val current by viewModel.currentExerciseFlow.collectAsStateWithLifecycle(null)

    LaunchedEffect(currentExerciseId) {
        viewModel.initWithCurrentExerciseId(currentExerciseId)
    }
    //println("VM: $viewModel")
    val state by viewModel.state.collectAsStateWithLifecycle()
    ExercisePickerContent(
        state = state,
        filteredExercises = filtered,
        currentExercise = current,
        onResult = onResult,
        onAction = { action -> viewModel.onAction(action) },
        onNavToExerciseEditor = { exerciseId ->
            appNavigator.navigateToExerciseEditor(exerciseId)
        }
    )
}

@Composable
fun ExercisePickerContent(
    state: ExercisePickerUiState,
    filteredExercises: List<ExercisePickerListItem>,
    currentExercise: ExercisePickerListItem?,
    onResult: (ExerciseId?) -> Unit,
    onAction: (ExercisePickerAction) -> Unit,
    onNavToExerciseEditor: (ExerciseId) -> Unit,
) {
    println("Recomposition ExercisePickerContent, selected : ${state.selectedExerciseId}")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { /* consume click */ }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.99f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onResult(null) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "close"
                        )
                    }
                }
                CurrentExerciseBar(currentExercise)
                Spacer(Modifier.height(16.dp))
                FiltersSection(state, filteredExercises, onAction)
                Spacer(Modifier.height(12.dp))
                SearchBar(
                    query = state.query,
                    onQueryChange = { onAction(ExercisePickerAction.Search(it)) }
                )
                Spacer(Modifier.height(12.dp))
                ExerciseList(
                    exercises = filteredExercises,
                    selectedExerciseId = state.selectedExerciseId,
                    scrollToIdx = state.scrollToIdx,
                    onAction = onAction,
                    onNavToExerciseEditor = onNavToExerciseEditor
                )
            }
        }
        //celowe: Ukrywaj przycisk, jeśli selected nie jest w filtrze
        //mozliwe ze user zrobił literówkę, którą zechce poprawić, i wtedy będzie miał dalej zaznaczony element
        //nie ma potrzeby sygnalizacji ukrytej selekcji
        val isSelectedVisible = filteredExercises.any { it.exerciseId == state.selectedExerciseId }
        if (state.selectedExerciseId != null && isSelectedVisible) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        onResult(state.selectedExerciseId)
                    },
                    //colors = buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.btn_choose))
                }
            }
        } else {
            FloatingActionButton(
                onClick = { onNavToExerciseEditor(ExerciseId.Custom.NEW) },
                modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-16).dp),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.btn_create)
                )
            }
        }
    }
    if (state.exercisePreview != null) {
        ExercisePickerExeInfo(
            exerciseInfo = state.exercisePreview,
            onClose = { onAction(ExercisePickerAction.ExercisePreviewClose) }
        )
    }
    if (state.showExpandedFilters) {
        ExercisePickerFilters(
            muscleGroups = state.muscleGroups,
            equipments = state.equipments,
            exerciseTypes = state.exerciseTypes,
            onClose = { onAction(ExercisePickerAction.CloseFilters) },
            onSaveFilters = { mg, equ, ts ->
                onAction(ExercisePickerAction.SetFilters(mg, equ, ts))
            }
        )
    }
}


@Composable
private fun CurrentExerciseBar(
    currentExercise: ExercisePickerListItem?,
) {
    if (currentExercise == null) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        //horizontalArrangement = Arrangement.SpaceBetween,
        //verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExerciseItem(
                item = currentExercise,
                actionsVisible = false,
                selected = false,
                onAction = { },
                onNavToExerciseEditor = { },
            )
            Text(
                text = stringResource(Res.string.exercise_exchange_with),//"Zamień na...",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Szukaj ćwiczeń") },
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = "search"
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun FiltersSection(
    state: ExercisePickerUiState,
    filteredExercises: List<ExercisePickerListItem>,
    onAction: (ExercisePickerAction) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = true,
            onClick = { onAction(ExercisePickerAction.ExpandFilters) },
            label = {
                Text(
                    if (state.isFilterEmpty) {
                        stringResource(
                            Res.string.exercise_exercises_all,
                            state.allExercises.size
                        )
                    } else {
                        stringResource(
                            Res.string.exercise_exercises_filtered,
                            filteredExercises.size
                        )
                    }
                )
            }
        )

        TextButton(
            onClick = { onAction(ExercisePickerAction.ClearFilters) }
        ) {
            Text(stringResource(Res.string.exercise_clear))
        }
    }

    Spacer(Modifier.height(8.dp))

    if (state.muscleGroups.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.muscleGroups.forEach { muscle ->
                AssistChip(
                    onClick = { onAction(ExercisePickerAction.RemoveMuscleFilter(muscle)) },
                    label = { Text(muscle.asUiText().asString()) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "close"
                        )
                    }
                )
            }
        }
    }
    if (state.equipments.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.equipments.forEach { equ ->
                AssistChip(
                    onClick = { onAction(ExercisePickerAction.RemoveEquipmentFilter(equ)) },
                    label = { Text(equ.asUiText().asString()) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "close"
                        )
                    }
                )
            }
        }
    }
    if (state.exerciseTypes.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.exerciseTypes.forEach { type ->
                AssistChip(
                    onClick = { onAction(ExercisePickerAction.RemoveExerciseTypeFilter(type)) },
                    label = { Text(type.asUiText().asString()) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "close"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ExerciseList(
    exercises: List<ExercisePickerListItem>,
    selectedExerciseId: ExerciseId?,
    scrollToIdx: Int?,
    onAction: (ExercisePickerAction) -> Unit,
    onNavToExerciseEditor: (ExerciseId) -> Unit,
) {
    println("ExerciseList: selected: $selectedExerciseId")
    val listState = rememberLazyListState()

    LaunchedEffect(scrollToIdx) {
        scrollToIdx?.let {
            listState.animateScrollToItem(scrollToIdx)
        }
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            count = exercises.size,
            key = { idx -> exercises[idx].exerciseId.asString() }
        ) { idx ->
            ExerciseItem(
                item = exercises[idx],
                actionsVisible = true,
                selected = exercises[idx].exerciseId == selectedExerciseId,
                onAction = onAction,
                onNavToExerciseEditor = onNavToExerciseEditor,
            )
        }
    }
}

@Composable
fun ExerciseItem(
    item: ExercisePickerListItem,
    actionsVisible: Boolean,
    selected: Boolean,
    onAction: (ExercisePickerAction) -> Unit,
    onNavToExerciseEditor: (ExerciseId) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction(ExercisePickerAction.ExerciseSelectionToggle(item.exerciseId)) }
        //.padding(vertical = 12.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 Ikona
        item.icon?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        // 🔹 Tekst
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold
            )

            //TODO - moze kiedyś uwzględnić tag typu "Podobne"
            /*Text(
                text = "Podobne",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )*/
        }

        /*// 🔹 Radio button
        RadioButton(
            selected = selected,
            onClick = { onAction(ExercisePickerAction.ExerciseSelectionToggle(item)) }
        )*/
        if (actionsVisible) {
            IconButton(onClick = { onAction(ExercisePickerAction.ExercisePreview(item.exerciseId)) }) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.ic_question_mark),
                        contentDescription = "choose",
                        //tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if (actionsVisible) {
            when (item.exerciseId) {
                is ExerciseId.BuiltIn -> IconButton(onClick = {
                    onNavToExerciseEditor(item.exerciseId)
                }) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.outlineVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(Res.drawable.ic_fork_right),
                            contentDescription = "fork",
                            //tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is ExerciseId.Custom -> IconButton(onClick = {
                    onNavToExerciseEditor(item.exerciseId)
                }) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.outlineVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(Res.drawable.ic_edit),
                            contentDescription = "edit",
                            //tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        if (actionsVisible) {
            IconButton(onClick = { onAction(ExercisePickerAction.ExerciseSelectionToggle(item.exerciseId)) }) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.outlineVariant),
                    contentAlignment = Alignment.Center
                )
                {
                    if (selected) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_checked),
                            contentDescription = "choose",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}


///////////////PREVIEW
val EXE_1 = ExercisePickerListItem(
    exerciseId = BuiltInExerciseId.PUSH_UP_HOLD.asExerciseId(),
    searchKey = "kod 1",
    name = "Loty na miotle",
    muscle = MuscleGroup.ARMS,
    equipment = Equipment.BODYWEIGHT,
    exerciseType = ExerciseType.STRENGTH,
    icon = Res.drawable.ic_flying_witch1,
    imagePath = null
)
val EXE_2 = ExercisePickerListItem(
    exerciseId = BuiltInExerciseId.FLUTTER_KICKS.asExerciseId(),
    searchKey = "kod 2",
    name = "Nożyce",
    muscle = MuscleGroup.ABS,
    equipment = Equipment.KETTLEBELL,
    exerciseType = ExerciseType.CARDIO,
    icon = Res.drawable.ic_flutter_kicks,
    imagePath = null
)

val EXE_CURR = ExercisePickerListItem(
    exerciseId = BuiltInExerciseId.BENT_LEG_TWIST.asExerciseId(),
    searchKey = "kod 3",
    name = "Bent Leg Twisted",
    muscle = MuscleGroup.BACK,
    equipment = Equipment.MACHINE,
    exerciseType = ExerciseType.MOBILITY,
    icon = Res.drawable.ic_bent_leg_twist,
    imagePath = null
)

@Preview
@Composable
private fun ExercisePickerContentPreview() {
    AppTheme {
        ExercisePickerContent(
            state = ExercisePickerUiState(
                query = "test",
            ),
            filteredExercises = listOf(
                EXE_1, EXE_2
            ),
            currentExercise = null,
            onResult = { },
            onAction = { },
            onNavToExerciseEditor = {},
        )
    }
}

@Preview
@Composable
private fun ExercisePickerContentPreview2() {
    AppTheme {
        ExercisePickerContent(
            state = ExercisePickerUiState(
                query = "test",
                muscleGroups = listOf(
                    MuscleGroup.ABS, MuscleGroup.BACK, MuscleGroup.CORE
                ),
                equipments = listOf(
                    Equipment.BODYWEIGHT, Equipment.BARBELL
                ),
                exerciseTypes = listOf(
                    ExerciseType.STRETCH
                ),
                selectedExerciseId = EXE_2.exerciseId,
            ),
            filteredExercises = listOf(
                EXE_1, EXE_2
            ),
            currentExercise = EXE_CURR,
            onResult = { },
            onAction = { },
            onNavToExerciseEditor = {},
        )
    }
}