package com.pl.myworkoutapp.ui.common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


sealed interface UiText {
    data class DynamicString(val value: String): UiText
    class StringResourceId(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(resource = id, formatArgs = args)
        }
    }
}

fun StringResource.asUiText(vararg args: Any) = UiText.StringResourceId(this, arrayOf(args))


// funkcja raczej nie powinna normalnie być użyta,
// jedyne zastosowanie jakie przychodzi mi na myśl to gdy będziemy chcieli pokazać
// jakiś tekst "z zewnątrz", np z wyjątku lub z wywołania api
// normalnie powinny być prezentowane teksty podlegające tlumaczeniom czyli z użyciem StringResourceId
fun String.asUiText() = UiText.DynamicString(this)
