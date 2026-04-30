package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.github.sarxos.webcam.*
import java.awt.BorderLayout
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JButton
import javax.swing.JPanel

//więcej info: https://github.com/sarxos/webcam-capture?tab=readme-ov-file
@Composable
actual fun CameraScreenContent(
    onResult: (String?) -> Unit,
) {

    val webcam = remember {
        Webcam.getDefault().apply {
            viewSize = WebcamResolution.VGA.getSize()
        }
    }

    DisposableEffect(webcam) {
        onDispose {
            webcam?.close()
        }
    }

    val panel = remember {
        JPanel(BorderLayout()).apply {
            webcam?.open() //świadomie tutaj a nie w DisposableEffect
            val webcamPanel = WebcamPanel(webcam).apply {
                isFPSDisplayed = true
                isDisplayDebugInfo = true;
                isImageSizeDisplayed = true;
                isMirrored = true;
            }

            val button = JButton("Zrób zdjęcie").apply {
                addActionListener {
                    val image = webcam.image
                    val tempDir = System.getProperty("java.io.tmpdir")
                    val file = File(tempDir, "photo_${System.currentTimeMillis()}.png")
                    ImageIO.write(image, "PNG", file)
                    // parent powinien zamknąć dialog
                    onResult(file.absolutePath)
                }
            }

            add(webcamPanel, BorderLayout.CENTER)
            add(button, BorderLayout.SOUTH)
        }
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = {
            onResult(null)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SwingPanel(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f),
                factory = { panel }
            )
        }
    }
}