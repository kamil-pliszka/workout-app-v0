package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
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

@Composable
actual fun CameraScreenContent(
    onResult: (String?) -> Unit,
) {
    val pickerDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image =
                    didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

                val data = image?.let { UIImageJPEGRepresentation(it, 0.9) }

                val path = NSSearchPathForDirectoriesInDomains(
                    NSDocumentDirectory,
                    NSUserDomainMask,
                    true
                ).first() as String

                val filePath = "$path/photo_${NSDate().timeIntervalSince1970}.jpg"

                data?.writeToFile(filePath, true)

                picker.dismissViewControllerAnimated(true, null)
                onResult(filePath)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
                onResult(null)
            }
        }
    }

    var launched by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!launched) {
            launched = true
            val picker = UIImagePickerController().apply {
                sourceType =
                    UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                delegate = pickerDelegate
            }

            val root = UIApplication.sharedApplication.keyWindow?.rootViewController
            root?.presentViewController(picker, true, null)
        }
    }

}