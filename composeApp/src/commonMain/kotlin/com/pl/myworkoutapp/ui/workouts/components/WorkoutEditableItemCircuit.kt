package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.EmptyUiText
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.theme.PureGreen
import com.pl.myworkoutapp.ui.workouts.*
import myworkoutapplication.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource


//Row
// ├─ Timeline (fixed width)
// └─ Content
//     ├─ nazwa
@Composable
fun WorkoutEditableItemCircuit(
    circuit: CircuitUiItem,
    themeColor: Color,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                types = circuit.timeline,
                itemWidth = 24.dp
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                val title = circuit.title.asString()
                if (title.isBlank()) {
                    Row {
                        Text(
                            text = circuit.phase.asUiText().asString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = circuit.structure.getStructureDesc().asString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                } else {
                    Row {
                        Text(
                            text = circuit.phase.asUiText().asString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = circuit.structure.getStructureDesc().asString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    Row {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // 🔹 IKONY (overlay)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onEditClick
                ),
                painter = painterResource(Res.drawable.ic_edit),
                contentDescription = "edit",
                //tint = MaterialTheme.colorScheme.primary
            )
            Icon(
                modifier = Modifier.clickable(
                    onClick = onDeleteClick
                ),
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = "delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }

}


val CIRCUIT_EDITABLE_ITEM_WM = CircuitUiItem(
    phase = Phase.WARMUP,
    structure = structureStandard(2),
    title = "ROZGRZEWECZKA".asUiText(),
)


@Preview
@Composable
fun CircuitEditablePreviewBasic() {
    AppTheme {
        WorkoutEditableItemCircuit(
            circuit = CIRCUIT_EDITABLE_ITEM_WM.copy(
                structure = structureAMRAP(12)
            ),
            themeColor = PureGreen,
            onClick = { },
            onEditClick = { },
            onDeleteClick = { },
        )
    }
}

@Preview
@Composable
fun CircuitEditablePreviewTimeLine() {
    AppTheme {
        WorkoutEditableItemCircuit(
            circuit = CIRCUIT_EDITABLE_ITEM_WM.copy(
                structure = structureEMOM(12)
            ).with(
                TimeLineItemType.Vertical(Color.Green),
                TimeLineItemType.Triple(Color.Magenta),
                TimeLineItemType.End(Color.Red),
            ),
            themeColor = PureGreen,
            onClick = { },
            onEditClick = { },
            onDeleteClick = { },
        )
    }
}

@Preview
@Composable
fun CircuitEditablePreviewProgress1() {
    AppTheme {

        WorkoutEditableItemCircuit(
            circuit = CIRCUIT_EDITABLE_ITEM_WM.copy(
                structure = structureStandard(7),
            ).with(
                TimeLineItemType.End(Color.Red),
            ),
            themeColor = PureGreen,
            onClick = { },
            onEditClick = { },
            onDeleteClick = { },
        )
    }
}

@Preview
@Composable
fun CircuitEditablePreviewProgress2() {
    AppTheme {
        WorkoutEditableItemCircuit(
            circuit = CIRCUIT_EDITABLE_ITEM_WM.copy(
                title = EmptyUiText,
                structure = structureTabata(
                    rounds = 8,
                    workSec = 30,
                    restSec = 15
                )
            ).with(
                TimeLineItemType.Triple(Color.Red),
            ),
            themeColor = PureGreen,
            onClick = { },
            onEditClick = { },
            onDeleteClick = { },
        )
    }
}
