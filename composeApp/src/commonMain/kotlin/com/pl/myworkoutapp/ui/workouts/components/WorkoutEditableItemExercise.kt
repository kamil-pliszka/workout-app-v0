package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.exercise.QuantityType
import com.pl.myworkoutapp.domain.model.exercise.asExerciseId
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.exercises.qtyValueAsUiText
import com.pl.myworkoutapp.ui.theme.*
import com.pl.myworkoutapp.ui.workouts.*
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutEditableItemExercise(
    exercise: ExerciseUiItem,
    themeColor: Color,
    onClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    quantityChangeAction: (increase: Boolean) -> Unit,
    ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            //.background(color = DesertWhite)
            .clickable { onClick() }
    ) {
        // 🔹 GŁÓWNY CONTENT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.height(IntrinsicSize.Min)
                .padding(end = 64.dp), // zostaw miejsce na ikony
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 LEWA STRONA (timeline)
            TimeLinePart(
                types = exercise.timeline,
                itemWidth = 24.dp
            )

            // 🔹 obrazek
            WorkoutEditableItemExerciseCard(
                //name = exercise.name.asString(),
                //qty = exercise.quantityValue.qtyValueAsUiText(exercise.quantityType).asString(),
                icon = exercise.icon,
                themeColor = themeColor,
            )

            // 🔹 Nazwa ćwiczenia/przyciski -+
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = exercise.name.asString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    //fontWeight = FontWeight.Bold,
                )
                SmallQuantityPicker(
                    value = exercise.quantityValue.qtyValueAsUiText(exercise.quantityType).asString(),
                    themeColor = themeColor,
                    onValueChange = quantityChangeAction
                )
            }
        }

        // 🔹 IKONY (overlay)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 8.dp, vertical = 8.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onExchangeClick
                ),
                painter = painterResource(Res.drawable.ic_exchange),
                contentDescription = "exchange",
                //tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.clickable(
                    onClick = onDeleteClick
                ),
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = "edit",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun WorkoutEditableItemExerciseCard(
    icon: DrawableResource,
    themeColor: Color,
) {
    Box(
        modifier = Modifier
            .padding(start = 0.dp)
            //.fillMaxWidth()
            .height(80.dp)
            .width(80.dp)
            .border(1.dp,
                themeColor, //MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            modifier = Modifier.widthIn(max = 80.dp).align(Alignment.Center),
            painter = painterResource(icon),
            contentDescription = "exe",
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun SmallQuantityPicker(
    value: String,
    themeColor: Color,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        //modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { onValueChange(false) }) {
            Icon(
                //imageVector = Icons.Filled.IndeterminateCheckBox,
                painter = painterResource(Res.drawable.ic_outline_indeterminate_check_box_24),
                contentDescription = "Decrease",
                tint = themeColor.copy(alpha = 0.5f),
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = RobotoItalicVariable,
        )
        IconButton(onClick = { onValueChange(true) }) {
            Icon(
                //imageVector = Icons.Filled.AddBox,
                painter = painterResource(Res.drawable.ic_outline_add_box_24),
                contentDescription = "Increase",
                tint = themeColor.copy(alpha = 0.5f),
            )
        }
    }
}

val EDITABLE_EXERCISE_ITEM_1 = ExerciseUiItem(
    isCurrent = false,
    isDone = true,
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Loty na miotle długie".asUiText(),
    icon = Res.drawable.ic_triceps_dip_on_chair,
)
val EDITABLE_EXERCISE_ITEM_2 = ExerciseUiItem(
    isCurrent = false,
    isDone = true,
    exerciseId = 123L.asExerciseId(),
    quantityType = QuantityType.REPS,
    quantityValue = 13,
    name = "Loty na miotle długie".asUiText(),
    icon = Res.drawable.ic_bent_leg_twist,
)

@Preview
@Composable
fun ExerciseEditablePreviewBasic() {
    AppTheme {
        WorkoutEditableItemExercise(
            exercise = EDITABLE_EXERCISE_ITEM_1.with(
                TimeLineItemType.Vertical(Color.Green)
            ),
            themeColor = PureGreen,
            onClick = { },
            onDeleteClick = { },
            onExchangeClick = { },
            quantityChangeAction = { },
        )
    }
}

@Preview
@Composable
fun ExerciseEditablePreviewTimeLine() {
    AppTheme {
        WorkoutEditableItemExercise(
            exercise = EDITABLE_EXERCISE_ITEM_1.with(
                TimeLineItemType.Vertical(Color.Green),
                TimeLineItemType.Triple(Color.Magenta),
                TimeLineItemType.End(Color.Red),
            ),
            themeColor = StrawberryRed,
            onClick = { },
            onDeleteClick = { },
            onExchangeClick = { },
            quantityChangeAction = { },
        )
    }
}