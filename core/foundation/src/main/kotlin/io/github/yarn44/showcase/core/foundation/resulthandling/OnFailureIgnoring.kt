package io.github.yarn44.showcase.core.foundation.resulthandling

import kotlin.reflect.KClass

/**
 * Executes [action] on failure, but ignores exceptions whose class
 * is in the [ignored] list.
 *
 * Useful for handling errors while skipping expected exceptions
 * like [CancellationException].
 */
inline fun <T> Result<T>.onFailureIgnoring(
    ignored: List<KClass<out Throwable>> = ignoredExceptionClasses,
    action: (Throwable) -> Unit,
): Result<T> {
    onFailure { exception ->
        if (ignored.none { it.isInstance(exception) }) {
            action(exception)
        }
    }
    return this
}
