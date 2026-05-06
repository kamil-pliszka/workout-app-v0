package com.pl.myworkoutapp.ui.common

import org.jetbrains.compose.resources.DrawableResource

sealed interface UiImage {
    data class LocalImage(val path: String) : UiImage

    data class ImageResource(
        val resource: DrawableResource,
    ) : UiImage

    object Empty : UiImage

    fun isEmpty() = this == Empty
}

fun DrawableResource.asUiImage() = UiImage.ImageResource(this)
fun String.asUiImage() = UiImage.LocalImage(this)

fun UiImage.localImagePath() : String? = if (this is UiImage.LocalImage) this.path else null