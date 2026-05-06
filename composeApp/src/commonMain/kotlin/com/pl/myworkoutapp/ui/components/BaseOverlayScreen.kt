package com.pl.myworkoutapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


//OVERLAY SCREEN, na podstawie CircuitEditorScreen
@Composable
fun BaseOverlayScreen(
    headerContent: @Composable RowScope.() -> Unit,
    mainContent: @Composable BoxScope.() -> Unit,
    bottomContent: @Composable RowScope.() -> Unit,
    maxHeight: Float = 0.9f,
    onCancel: () -> Unit
) {
    // Backdrop / Scrim
    // brzydko, ale jednolicie z pozostałymi ekranami
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCancel() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(maxHeight) // Slightly less than 1.0 to show it's an overlay
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .imePadding() // Ensures UI moves up when keyboard appears
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* Consume clicks to prevent closing */ }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                headerContent()
            }

            // Content area
            Box(modifier = Modifier.weight(1f)) {
                mainContent()
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)


            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                ) {
                    bottomContent()
                }
            }
        }
    }
}
