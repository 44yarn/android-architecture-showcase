package io.github.yarn44.showcase.core.uikit.indicator

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

/**
 * Centralized loading state management.
 *
 * Wraps async operations with [withLoadingResult] to automatically
 * manage isLoading state, ensuring it is always reset even on
 * cancellation (via finally block).
 */
@Stable
class IndicatorState @Inject constructor() {
    var isLoading: Boolean by mutableStateOf(false)
        private set

    fun startLoading() {
        isLoading = true
    }

    fun stopLoading() {
        isLoading = false
    }

    /**
     * Starts loading, executes [block], then stops loading.
     * The finally block ensures loading stops even when the coroutine is cancelled.
     *
     * Note: Do NOT chain onSuccess/onFailure inside [block] — callbacks would
     * execute before loading stops. Chain them on the returned Result instead.
     */
    suspend fun <T> withLoadingResult(block: suspend () -> Result<T>): Result<T> {
        isLoading = true
        return try {
            block().also { isLoading = false }
        } finally {
            isLoading = false
        }
    }
}
