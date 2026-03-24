package io.github.yarn44.showcase.core.uikit.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Unifies string resource IDs and plain strings into a single type.
 * This allows ViewModels to provide UI text without depending on Context.
 */
class AdaptiveString private constructor(
    private val text: String?,
    @StringRes private val resId: Int?,
    private val formatArgs: Array<out Any>?,
) {
    constructor(text: String) : this(text = text, resId = null, formatArgs = null)

    constructor(@StringRes resId: Int) : this(text = null, resId = resId, formatArgs = null)

    constructor(
        @StringRes resId: Int,
        vararg formatArgs: Any,
    ) : this(text = null, resId = resId, formatArgs = formatArgs)

    /** Resolves the string in a Composable context. */
    val value: String
        @Composable
        get() = resolveString(LocalContext.current)

    /** Resolves the string with an explicit [Context]. */
    fun resolveString(context: Context): String = when {
        text != null -> text
        resId != null && formatArgs != null -> context.getString(resId, *formatArgs)
        resId != null -> context.getString(resId)
        else -> ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdaptiveString) return false
        return text == other.text &&
            resId == other.resId &&
            formatArgs.contentEquals(other.formatArgs)
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + resId.hashCode()
        result = 31 * result + formatArgs.contentHashCode()
        return result
    }

    override fun toString(): String = text ?: "AdaptiveString(resId=$resId)"
}
