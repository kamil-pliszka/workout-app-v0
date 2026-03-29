package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePicker(
    onResult: (String?) -> Unit
): ImagePicker {

    val delegate = remember {
        object : NSObject(),
            UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            var callback: ((String?) -> Unit)? = null

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image = didFinishPickingMediaWithInfo[
                    UIImagePickerControllerOriginalImage
                ] as? UIImage

                val data = image?.let {
                    UIImageJPEGRepresentation(it, 1.0)
                }

                val path = data?.let {
                    saveToTempFile(it)
                }

                callback?.invoke(path)
                picker.dismissViewControllerAnimated(true, null)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                callback?.invoke(null)
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        object : ImagePicker {
            override fun pickImage() {
                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary

                delegate.callback = onResult
                picker.delegate = delegate

                val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
                rootVC?.presentViewController(picker, true, null)
            }
        }
    }
}

fun saveToTempFile(data: NSData): String? {
    val tempDir = NSTemporaryDirectory()
    val filePath = tempDir + "img_${NSDate().timeIntervalSince1970}.png"

    return try {
        data.writeToFile(filePath, true)
        filePath
    } catch (e: Exception) {
        null
    }
}