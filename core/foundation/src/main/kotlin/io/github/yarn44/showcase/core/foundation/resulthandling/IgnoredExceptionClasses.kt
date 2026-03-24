package io.github.yarn44.showcase.core.foundation.resulthandling

import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

/**
 * Exception classes that should be ignored in error logging.
 * [CancellationException] is expected during normal coroutine lifecycle
 * and should not be treated as an application error.
 */
val ignoredExceptionClasses: List<KClass<out Throwable>> = listOf(
    CancellationException::class,
)
