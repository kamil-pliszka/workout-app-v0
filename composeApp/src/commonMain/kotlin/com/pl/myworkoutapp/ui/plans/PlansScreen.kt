package com.pl.myworkoutapp.ui.plans

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlansScreen(
    state: PlansUiState,
    onAction: (PlansAction) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
        Text("Plans")

        TextButton(onClick = { onAction(PlansAction.NavToWorkout("xxxrrrrr")) }) {
            Text("Navigate TO workout exec")
        }

    }
}