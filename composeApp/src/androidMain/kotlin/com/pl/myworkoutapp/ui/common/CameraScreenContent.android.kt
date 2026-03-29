package com.pl.myworkoutapp.ui.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun CameraScreenContent(
    onResult: (String?) -> Unit,
) {
    val context = LocalContext.current

    val capturedFile = remember {
        val timestamp = System.currentTimeMillis()
        File(context.filesDir, "_tmp_camera_$timestamp.jpeg")
    }

//    if (LocalInspectionMode.current) {//jeśli preview w android studio
//        return remember {
//            object : CameraController {
//                override fun takePhoto() {
//                    onResult("/preview/fake.jpg")
//                }
//            }
//        }
//    }

    val uri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            capturedFile
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onResult(capturedFile.absolutePath)
        } else {
            onResult(null)
        }
    }

    var launched by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!launched) {
            launched = true
            launcher.launch(uri)
        }
    }
}