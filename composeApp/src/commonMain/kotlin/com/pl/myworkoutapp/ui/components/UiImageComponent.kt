package com.pl.myworkoutapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pl.myworkoutapp.ui.common.*
import com.pl.myworkoutapp.ui.theme.AppTheme
import com.pl.myworkoutapp.ui.theme.LuminousGreen
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.ic_plank1
import org.jetbrains.compose.resources.painterResource

@Composable
fun UiImageComponent(
    modifier: Modifier,
    contentDescription: String? = null,
    image: UiImage,
    tint: Color? = null,
    contentScale: ContentScale = ContentScale.Fit,
    emptyImageContent: @Composable () -> Unit = {},
    noLocalImageContent: @Composable () -> Unit = {},
) {

    when (image) {
        UiImage.Empty -> emptyImageContent()
        is UiImage.ImageResource -> {
            /*Icon(
                painter = painterResource(image.resource),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint ?: LocalContentColor.current,
            )*/
            Image(
                painter = painterResource(image.resource),
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = if (tint == null) null else ColorFilter.tint(tint),
                contentScale = contentScale
            )
        }

        is UiImage.LocalImage -> {
            val bitmap = remember(image.path) {
                loadImageBitmap(image.path)
            }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    colorFilter = if (tint == null) null else ColorFilter.tint(tint),
                    contentScale = contentScale
                )
            } else {
                noLocalImageContent()
            }
        }
    }
}

@Preview
@Composable
private fun UiImageCompPreview1() {
    AppTheme {
        UiImageComponent(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 180.dp),
            contentDescription = "exercise image",
            image = Res.drawable.ic_plank1.asUiImage(),
            tint = LuminousGreen.copy(alpha = 0.5f),
        )
    }
}