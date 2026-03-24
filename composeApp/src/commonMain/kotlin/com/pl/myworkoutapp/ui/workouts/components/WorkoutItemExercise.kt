package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.theme.DesertWhite
import com.pl.myworkoutapp.ui.theme.PureGreen
import com.pl.myworkoutapp.ui.workouts.ExerciseUiItem
import com.pl.myworkoutapp.ui.workouts.TimeLineItemType
import com.pl.myworkoutapp.ui.workouts.with
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
fun WorkoutItemExercise(
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
            Text(
                text = exercise.name.asString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            //Spacer(Modifier.height(8.dp))
            ExerciseCard(
                qty = exercise.quantityText.asString(),
                icon = exercise.icon,
                isDone = exercise.isDone,
                isCurrent = exercise.isCurrent,
                themeColor = themeColor,
            )
            Spacer(Modifier.height(8.dp))
        }
        //Spacer(Modifier.width(4.dp))
    }
}

val EXERCISE_ITEM = ExerciseUiItem(
    isCurrent = false,
    isDone = true,
    exerciseId = 123.asExerciseId(),
    quantityText = "do świtu".asUiText(),
    name = "Lot na miotle".asUiText(),
    icon = Res.drawable.ic_flying_witch,
)

@Preview
@Composable
fun ExercisePreviewBasic() {
    WorkoutItemExercise(
        exercise = EXERCISE_ITEM,
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun ExercisePreviewTimeLine() {
    WorkoutItemExercise(
        exercise = EXERCISE_ITEM.with(
            TimeLineItemType.Vertical(Color.Green),
            TimeLineItemType.None(),
            TimeLineItemType.Triple(Color.Magenta),
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}