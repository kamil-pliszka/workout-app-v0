package com.pl.myworkoutapp.ui.common

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun loadImageBitmap(path: String): ImageBitmap? {
    val bitmap = BitmapFactory.decodeFile(path) ?: return null
    return bitmap.asImageBitmap()
}