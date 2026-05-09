package com.pl.myworkoutapp.ui.common

import org.jetbrains.compose.resources.DrawableResource

sealed interface UiImage {
    data class LocalImage(val path: String) : UiImage

    data class ImageResource(
        val resource: DrawableResource,
    ) : UiImage

    object Empty : UiImage

    fun isEmpty() = this == Empty
    val UiImage.isNotEmpty: Boolean
        get() = this != Empty

    fun isLocalImage() = this is LocalImage

    companion object {
        fun of(
            imageRes: DrawableResource?,
            imagePath: String?,
        ): UiImage {
            return when {
                !imagePath.isNullOrEmpty() -> LocalImage(imagePath)
                imageRes != null -> ImageResource(imageRes)
                else -> Empty
            }
        }
    }
}

fun DrawableResource.asUiImage() = UiImage.ImageResource(this)
fun String?.asUiImage() = if (this.isNullOrEmpty()) UiImage.Empty else UiImage.LocalImage(this)

fun UiImage.localImagePath() : String? = if (this is UiImage.LocalImage) this.path else null