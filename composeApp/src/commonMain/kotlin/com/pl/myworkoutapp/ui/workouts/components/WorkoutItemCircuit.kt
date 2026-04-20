package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.domain.model.workout.CircuitStructure
import com.pl.myworkoutapp.domain.model.workout.Phase
import com.pl.myworkoutapp.ui.common.asUiText
import com.pl.myworkoutapp.ui.components.ProgressCircle
import com.pl.myworkoutapp.ui.components.SegmentedProgressIndicator
import com.pl.myworkoutapp.ui.theme.DesertWhite
import com.pl.myworkoutapp.ui.theme.PureGreen
import com.pl.myworkoutapp.ui.workouts.*
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_done_all
import org.jetbrains.compose.resources.painterResource


//Row
// ├─ Timeline (fixed width)
// │   ├─ kropka
// │   └─ przerywana linia (Canvas)
// └─ Content
//     ├─ nazwa + progress
@Composable
fun WorkoutItemCircuit(
    circuit: CircuitUiItem,
    //timeline: List<TimeLineItemType>,
    themeColor: Color,
    onClick: () -> Unit
) {
    val crounds = when (circuit.structure) {
        is CircuitStructure.AMRAP -> 1
        is CircuitStructure.EMOM -> 1
        is CircuitStructure.Standard -> circuit.structure.rounds
        is CircuitStructure.Tabata -> circuit.structure.rounds
    }
    Row(
        modifier = Modifier.fillMaxWidth().background(color = DesertWhite).height(IntrinsicSize.Min)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 LEWA STRONA (timeline)
        TimeLinePart(
            types = circuit.timeline,
            itemWidth = 24.dp
        )
        Column {
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
            val title = circuit.title.asString()
            if (title.isNotBlank()) {
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
        println("circuit: $circuit")
        if (circuit.isDone) {
            Icon(
                painter = painterResource(Res.drawable.ic_done_all),
                contentDescription = "Well done",
                tint = themeColor,
                modifier = Modifier.scale(0.7f)
            )
        } else {
            println("progress: ${circuit.progress}")
            circuit.progress?.let {
                if (crounds <= 8) {
                    println("jestem tu")
                    SegmentedProgressIndicator(
                        progress = circuit.progress,
                        modifier = Modifier.width((16 * crounds).dp)
                            .padding(end = 8.dp), //.fillMaxWidth(),
                        color = themeColor,
                        //backgroundColor = Color.LightGray,
                        progressHeight = 6.dp,
                        numberOfSegments = crounds,
                        segmentGap = 4.dp
                    )
                } else {//zbyt wiele żeby pokazać
                    ProgressCircle(
                        progress = circuit.progress,
                        strokeWidth = 3.dp,
                        trackColor = themeColor,
                        modifier = Modifier.padding(end = 8.dp).padding(vertical = 4.dp),
                        size = 32.dp
                    )
                }
            }
        }
    }
}


val CIRCUIT_ITEM_WM = CircuitUiItem(
    isCurrent = false,
    isDone = true,
    phase = Phase.WARMUP,
    structure = CircuitStructure.Standard(2),
    title = "ROZGRZEWECZKA".asUiText(),
)


@Preview
@Composable
fun CircuitPreviewBasic() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM.copy(
            structure = CircuitStructure.AMRAP(300)
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun CircuitPreviewTimeLine() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM.copy(
            structure = CircuitStructure.EMOM(12)
        ).with(
            TimeLineItemType.Vertical(Color.Green),
            TimeLineItemType.Triple(Color.Magenta),
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun CircuitPreviewProgress1() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM.copy(
            structure = CircuitStructure.Standard(7),
            isDone = false,
            progress = 0.4f
        ).with(
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun CircuitPreviewProgress2() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM.copy(
            structure = CircuitStructure.Tabata(
                rounds = 8,
                workSec = 30,
                restSec = 15
            ),
            isDone = false,
            progress = 0.4f
        ).with(
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}

