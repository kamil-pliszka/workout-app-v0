package com.pl.myworkoutapp.ui.common

import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
actual fun rememberImagePicker(
    onResult: (String?) -> Unit
): ImagePicker {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        scope.launch(Dispatchers.IO) {
            val path = uri?.let { u ->
                try {
                    val resolver = context.contentResolver

                    val mime = resolver.getType(u)
                    val ext = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(mime) ?: "tmp"

                    val file = File(
                        context.cacheDir,
                        "img_${System.currentTimeMillis()}.$ext"
                    )

                    resolver.openInputStream(u)?.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }

                    file.absolutePath
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            withContext(Dispatchers.Main) {
                onResult(path)
            }
        }
    }

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                launcher.launch("image/*")
            }
        }
    }
}