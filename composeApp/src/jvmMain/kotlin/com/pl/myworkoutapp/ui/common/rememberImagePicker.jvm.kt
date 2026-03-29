package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberImagePicker(
    onResult: (String?) -> Unit
): ImagePicker {

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                val chooser = JFileChooser()

                chooser.fileFilter = FileNameExtensionFilter(
                    "Images", "jpg", "jpeg", "png", "webp"
                )

                val result = chooser.showOpenDialog(null)

                if (result == JFileChooser.APPROVE_OPTION) {
                    val file: File = chooser.selectedFile
                    onResult(file.absolutePath)
                } else {
                    onResult(null)
                }
            }
        }
    }
}