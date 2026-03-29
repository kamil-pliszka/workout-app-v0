package com.pl.myworkoutapp.ui.common

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun loadImageBitmap(path: String): ImageBitmap? {
    val nsData: NSData = NSData.dataWithContentsOfFile(path) ?: return null

    val bytes = ByteArray(nsData.length.toInt())
    bytes.usePinned { pinned ->
        platform.posix.memcpy(
            pinned.addressOf(0),
            nsData.bytes,
            nsData.length
        )
    }

    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}