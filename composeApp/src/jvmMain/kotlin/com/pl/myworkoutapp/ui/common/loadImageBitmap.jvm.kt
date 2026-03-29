package com.pl.myworkoutapp.ui.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.io.File

actual fun loadImageBitmap(path: String): ImageBitmap? {
    val file = File(path)
    if (!file.exists()) return null

    val bytes = file.readBytes()
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}