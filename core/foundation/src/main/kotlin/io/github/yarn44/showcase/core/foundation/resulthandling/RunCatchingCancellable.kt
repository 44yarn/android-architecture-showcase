package io.github.yarn44.showcase.core.foundation.resulthandling

import kotlin.coroutines.cancellation.CancellationException

/**
 * A safe alternative to [runCatching] that re-throws [CancellationException]
 * instead of wrapping it in [Result.failure].
 *
 * Standard [runCatching] catches ALL exceptions including [CancellationException],
 * which breaks coroutine cancellation propagation. This function preserves
 * the cancellation contract by re-throwing [CancellationException].
 */
suspend fun <T> runCatchingCancellable(block: suspend () -> T): Result<T> =
    runCatching { block() }.also { result ->
        result.exceptionOrNull()
            ?.takeIf { it is CancellationException }
            ?.let { throw it }
    }
