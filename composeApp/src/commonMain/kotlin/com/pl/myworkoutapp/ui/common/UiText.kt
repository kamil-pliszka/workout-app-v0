package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.*

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

    suspend fun loadString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResourceId -> {
                val resolvedArgs = args.map { if (it is UiText) it.loadString() else it }
                getString(resource = id, formatArgs = resolvedArgs.toTypedArray())
            }

            is Empty -> ""
        }
    }

    fun isEmpty() = when (this) {
        is DynamicString -> this.value.isEmpty()
        Empty -> true
        is StringResourceId -> false
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

//@Composable
//fun List<UiText>.joinAsString(separator: String = "\n"): String {
//    return joinToString(separator) { it.asString() }
//}

suspend fun List<UiText>.joinToString(separator: String = "\n"): String {
    val strings = map { it.loadString() }
    return strings.joinToString(separator)
}
