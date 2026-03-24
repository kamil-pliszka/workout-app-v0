package com.pl.myworkoutapp.ui.workouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.pl.myworkoutapp.ui.workouts.CircuitUiItem
import com.pl.myworkoutapp.ui.workouts.TimeLineItemType
import com.pl.myworkoutapp.ui.workouts.with
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.done_all
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
    Row(
        modifier = Modifier.fillMaxWidth().background(color = DesertWhite).height(IntrinsicSize.Min).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 LEWA STRONA (timeline)
        TimeLinePart(
            types = circuit.timeline,
            itemWidth = 24.dp
        )
        Text(
            text = circuit.title.asString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        if (circuit.rounds > 1) {
            Text(
                text = " : x${circuit.rounds}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        println("circuit: $circuit")
        if (circuit.isDone) {
            Icon(
                painter = painterResource(Res.drawable.done_all),
                contentDescription = "Well done",
                tint = themeColor,
                modifier = Modifier.scale(0.7f)
            )
        } else {
            println("progress: ${circuit.progress}")
            circuit.progress?.let {
                if (circuit.rounds <= 8) {
                    println("jestem tu")
                    SegmentedProgressIndicator(
                        progress = circuit.progress,
                        modifier = Modifier.width((16 * circuit.rounds).dp)
                            .padding(end = 8.dp), //.fillMaxWidth(),
                        color = themeColor,
                        //backgroundColor = Color.LightGray,
                        progressHeight = 6.dp,
                        numberOfSegments = circuit.rounds,
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
        //TODO - phase?
        //TODO - structure?
    }
}



val CIRCUIT_ITEM_WM = CircuitUiItem(
    isCurrent = false,
    isDone = true,
    phase = Phase.WARMUP,
    rounds = 2,
    structure = CircuitStructure.Standard,
    title = "ROZGRZEWECZKA".asUiText(),
)


@Preview
@Composable
fun CircuitPreviewBasic() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM,
        themeColor = PureGreen,
        onClick = { }
    )
}

@Preview
@Composable
fun CircuitPreviewTimeLine() {
    WorkoutItemCircuit(
        circuit = CIRCUIT_ITEM_WM.with(
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
        circuit = CIRCUIT_ITEM_WM.copy(rounds = 8, isDone = false, progress = 0.4f).with(
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
        circuit = CIRCUIT_ITEM_WM.copy(rounds = 9, isDone = false, progress = 0.4f).with(
            TimeLineItemType.End(Color.Red),
        ),
        themeColor = PureGreen,
        onClick = { }
    )
}

