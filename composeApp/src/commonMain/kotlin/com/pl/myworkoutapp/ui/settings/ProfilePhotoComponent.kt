package com.pl.myworkoutapp.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.common.loadImageBitmap
import com.pl.myworkoutapp.ui.theme.AppTheme
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_face3
import myworkoutapplication.composeapp.generated.resources.profile_error_in_avatar_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ProfilePhotoComponent(
    photoPath: String?,
    tmpPhotoPath: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()     // dopasowuje wysokość do Row (czyli lewej kolumny)
            .aspectRatio(1f)     // wymusza kwadrat
        //.background(Color.Green)
        ,
        contentAlignment = Alignment.Center
    ) {
        when {
            tmpPhotoPath != null || photoPath != null -> {
                val photoBitmap = remember(tmpPhotoPath, photoPath) {
                    val imagePath = tmpPhotoPath ?: photoPath ?: error("empty photo")
                    println("Loading avatar from: $imagePath")
                    loadImageBitmap(imagePath)
                }
                if (photoBitmap != null) {
                    Image(
                        bitmap = photoBitmap,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            //.aspectRatio(1f)
                            .border(
                                3.5.dp,
                                MaterialTheme.colorScheme.secondary,
                                CircleShape
                            )
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(stringResource(Res.string.profile_error_in_avatar_image))
                }
            }
            /*state.isLoading -> {
                println("CircularProgressIndicator")
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }*/
            //state.photoPath == null -> {
            else -> {
                println("Load default avatar from resource")
                Icon(
                    painter = painterResource(Res.drawable.ic_face3),
                    contentDescription = "Avatar",
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfilePhotoComponentPreviewEmpty() {
    AppTheme {
        ProfilePhotoComponent(
            photoPath = null,
            tmpPhotoPath = null
        )
    }
}

@Preview
@Composable
fun ProfilePhotoComponentPreviewErr() {
    AppTheme {
        ProfilePhotoComponent(
            photoPath = null,
            tmpPhotoPath = "incorrect image path"
        )
    }
}
