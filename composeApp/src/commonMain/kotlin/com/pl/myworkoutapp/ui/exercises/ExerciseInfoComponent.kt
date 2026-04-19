package com.pl.myworkoutapp.ui.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.pl.myworkoutapp.domain.model.exercise.*
import com.pl.myworkoutapp.ui.common.loadExerciseDescription
import com.pl.myworkoutapp.ui.common.loadImageBitmap
import com.pl.myworkoutapp.ui.theme.*
import kotlinx.coroutines.runBlocking
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExerciseInfoComponent(
    exerciseInfo: ExerciseInfoUiModel,
    changeQtyButtons: Boolean = true,
    quantityChangeAction: (increase: Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                //.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = exerciseInfo.name.asString(),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = EurostileExt
            )

            exerciseInfo.icon?.let { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "exercise icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 160.dp),
                    tint = LuminousGreen.copy(alpha = 0.5f),
                )
            }
            exerciseInfo.imagePath?.let { imagePath ->
                val bitmap = remember(imagePath) {
                    loadImageBitmap(imagePath)
                }
                bitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "exercise image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 180.dp),
                    )
                }
            }

            if (exerciseInfo.quantityValue != null) {
                QuantityPicker(
                    label = exerciseInfo.quantityType.asUiText().asString(),
                    value = exerciseInfo.quantityValue.qtyValueAsUiText(exerciseInfo.quantityType).asString(),
                    showButtons = changeQtyButtons,
                    onValueChange = quantityChangeAction
                )
            }

            DescriptionSection(exerciseInfo)
        }
    }
}

@Composable
private fun QuantityPicker(
    label: String,
    value: String,
    showButtons: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = RobotoItalicVariable,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        if (showButtons) {
            IconButton(onClick = { onValueChange(false) }) {
                Icon(
                    //imageVector = Icons.Filled.IndeterminateCheckBox,
                    painter = painterResource(Res.drawable.ic_outline_indeterminate_check_box_24),
                    contentDescription = "Decrease",
                    tint = LuminousGreen.copy(alpha = 0.5f),
                )
            }
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = RobotoItalicVariable,
        )
        if (showButtons) {
            IconButton(onClick = { onValueChange(true) }) {
                Icon(
                    //imageVector = Icons.Filled.AddBox,
                    painter = painterResource(Res.drawable.ic_outline_add_box_24),
                    contentDescription = "Increase",
                    tint = LuminousGreen.copy(alpha = 0.5f),
                )
            }
        }
    }
}

@Composable
private fun DescriptionSection(exerciseInfo: ExerciseInfoUiModel) {
    if (exerciseInfo.customDesc != null) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(Res.string.exercise_description_label),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = RobotoItalicVariable,
                fontSize = 22.sp
            )
            Text(
                text = exerciseInfo.customDesc.asString(),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = RobotoItalicVariable,
            )
        }
    } else {
        /*
        val descriptionMarkdown by produceState<String?>(
            null,
            exerciseInfo.descExerciseId
        ) {
            value = exerciseInfo.descExerciseId?.let { id ->
                loadExerciseDescription(
                    exerciseId = id,
                    lang = Locale.current.language
                )
            }
        }*/
        exerciseInfo.descriptionMarkdown?.let { markdown ->
            val bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = RobotoItalicVariable)
            Markdown(
                content = markdown,
                typography = markdownTypography(
                    h3 = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = RobotoItalicVariable,
                        fontSize = 22.sp
                    ),
                    h4 = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = RobotoItalicVariable,
                        fontSize = 18.sp
                    ),
                    text = bodyLarge,
                    paragraph = bodyLarge,
                    ordered = bodyLarge,
                    bullet = bodyLarge,
                    list = bodyLarge,
                ),
            )
        }
    }
}

val EXE_B = BuiltInExerciseRegistry.get(BuiltInExerciseId.BENT_LEG_TWIST)
fun ExerciseInfoUiModel.loadExerciseMarkdownForPreview(): String? = this
    .takeIf { it.customDesc == null }
    ?.descExerciseId
    ?.let { id ->
        runBlocking {
            //error("lang=" + Locale.current.language)
            loadExerciseDescription(
                exerciseId = id,
                lang = Locale.current.language
            )
        }
    }


@Preview(locale = "pl")
@Composable
fun ExerciseInfoComponentBuiltinPL() {
    AppTheme {
        val exerciseInfo = EXE_B.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        ExerciseInfoComponent(
            exerciseInfo.copy(
                quantityValue = 17,
                descriptionMarkdown = markdown
            ),
            quantityChangeAction = {}
        )
    }
}

@Preview(locale = "en")
@Composable
fun ExerciseInfoComponentBuiltinEN() {
    AppTheme {
        val exerciseInfo = EXE_B.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        ExerciseInfoComponent(
            EXE_B.toUi().copy(
                quantityValue = 16,
                descriptionMarkdown = markdown
            ),
            quantityChangeAction = {}
        )
    }
}



val EXE_C = CustomExercise(
    id = 1234L.asExerciseId(),
    name = "NAJLEPSZE ĆWICZENIE",
    description = """
        Potraktuj to ćwiczenie jako dodatek do głównej jednostki treningowej.

        Połóż się na plecach, trzymając nogi wyprostowane, a ramiona uniesione w kierunku sufitu. 
        Następnie jednocześnie unieś nogi oraz górną część ciała, tak aby sylwetka przyjęła kształt łuku. 
        Wytrzymaj w tej pozycji przez około 10–15 sekund, po czym powoli opuść ciało z powrotem na podłoże. 
        Pamiętaj, aby przez cały czas odcinek lędźwiowy był dociśnięty do podłogi.
    """.trimIndent(),
    imageUri = null,
    basedOn = null,
    muscle = MuscleGroup.CORE,
    exerciseType = ExerciseType.CARDIO,
    equipment = Equipment.BODYWEIGHT,
    met = 1.23,
    quantityType = QuantityType.DURATION,
    defaultQuantityValue = 12,
)

@Preview
@Composable
fun ExerciseInfoComponentCustom() {
    AppTheme {
        ExerciseInfoComponent(
            EXE_C.toUi(),
            quantityChangeAction = {}
        )
    }
}


val EXE_CB = CustomExercise(
    id = 1234L.asExerciseId(),
    name = "NAJLEPSZE ĆWICZENIE BAZOWANE",
    description = null,
    imageUri = null,
    basedOn = BuiltInExerciseId.PLANK.asExerciseId(),
    muscle = MuscleGroup.CORE,
    exerciseType = ExerciseType.CARDIO,
    equipment = Equipment.BODYWEIGHT,
    met = 1.23,
    quantityType = QuantityType.DURATION,
    defaultQuantityValue = 12,
)

@Preview
@Composable
fun ExerciseInfoComponentCustomBased() {
    AppTheme {
        val exerciseInfo = EXE_CB.toUi()
        val markdown = exerciseInfo.loadExerciseMarkdownForPreview()
        ExerciseInfoComponent(
            EXE_CB.toUi().copy(
                quantityValue = 145,
                descriptionMarkdown = markdown
            ),
            quantityChangeAction = {}
        )
    }
}
