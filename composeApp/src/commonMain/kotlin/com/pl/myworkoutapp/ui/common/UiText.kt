package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Immutable
sealed interface UiText {
    data class DynamicString(val value: String) : UiText

    data class StringResourceId(
        val id: StringResource,
        val args: List<Any> = emptyList()
    ) : UiText

    object Empty : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResourceId -> {
                val resolvedArgs = args.map { if (it is UiText) it.asString() else it }
                stringResource(resource = id, formatArgs = resolvedArgs.toTypedArray())
            }
            is Empty -> ""
        }
    }
}

/**
 * Extension to easily wrap a StringResource into UiText.
 */
fun StringResource.asUiText(vararg args: Any) = UiText.StringResourceId(this, args.toList())

/**
 * Extension for raw strings (use sparingly, mostly for API/Error data).
 */
fun String?.asUiText() = this?.let { UiText.DynamicString(it) } ?: UiText.Empty

val EmptyUiText = UiText.Empty
