package io.github.yarn44.showcase.core.foundation.resulthandling

import kotlin.reflect.KClass
import timber.log.Timber

/**
 * Logs the exception via [Timber.e] on failure, skipping exceptions
 * in the [ignored] list (e.g. [CancellationException]).
 *
 * This ensures consistent error logging across the app while avoiding
 * noise from expected exceptions.
 */
fun <T> Result<T>.withErrorLog(
    ignored: List<KClass<out Throwable>> = ignoredExceptionClasses,
): Result<T> {
    onFailure { exception ->
        if (ignored.none { it.isInstance(exception) }) {
            Timber.e(exception)
        }
    }
    return this
}
