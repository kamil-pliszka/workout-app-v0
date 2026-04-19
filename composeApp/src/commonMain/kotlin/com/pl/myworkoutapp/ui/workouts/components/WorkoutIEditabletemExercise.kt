package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.exercises.qtyValueAsUiText
import com.pl.myworkoutapp.ui.theme.DesertWhite
import com.pl.myworkoutapp.ui.theme.PureGreen
import com.pl.myworkoutapp.ui.workouts.*
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_flying_witch

//Row
// ├─ Timeline (fixed width)
// │   ├─ kropka
// │   └─ przerywana linia (Canvas)
// └─ Content
//     ├─ nazwa
//     └─ card (image + badge)
@Composable
fun WorkoutEditableItemExercise(
    exercise: ExerciseUiItem,
    themeColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = DesertWhite).height(IntrinsicSize.Min).clickable { onClick() }
    ) {
        // 🔹 LEWA STRONA (timeline)
        TimeLinePart(
            types = exercise.timeline,
            itemWidth = 24.dp
        )
        //Spacer(Modifier.width(4.dp))
        // 🔹 PRAWA STRONA (content)
        Column(modifier = Modifier.weight(1f).padding(top = 2.dp)) {
            //Spacer(Modifier.height(8.dp))
            ExerciseCardEditable(
                name = exercise.name.asString(),
                qty = exercise.quantityValue.qtyValueAsUiText(exercise.quantityType).asString(),
                icon = exercise.icon,
                isDone = exercise.isDone,
                themeColor = themeColor,
            )
        }
        //Spacer(Modifier.width(4.dp))
    }
}

val EDITABLE_EXERCISE_ITEM = ExerciseUiItem(
    isCurrent = false,
    isDone = true,
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch,
)

@Preview
@Composable
fun ExerciseEditablePreviewBasic() {
    WorkoutItemExercise(
        exercise = EDITABLE_EXERCISE_ITEM,
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun ExerciseEditablePreviewTimeLine() {
    WorkoutItemExercise(
        exercise = EDITABLE_EXERCISE_ITEM.with(
            TimeLineItemType.Vertical(Color.Green),
            TimeLineItemType.None(),
            TimeLineItemType.Triple(Color.Magenta),
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}