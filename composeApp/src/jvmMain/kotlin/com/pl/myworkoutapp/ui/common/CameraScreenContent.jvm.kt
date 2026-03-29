package com.pl.myworkoutapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import com.github.sarxos.webcam.WebcamResolution
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