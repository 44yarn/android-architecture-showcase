package io.github.yarn44.showcase.core.uikit.indicator

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

/**
 * Centralized loading state management.
 *
 * Wraps async operations with [runWithLoading] to automatically
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
     *
     * Design intent for the dual reset (do NOT collapse into one):
     * - `block().also { isLoading = false }` is the **primary control for the normal
     *   flow**. When [block] returns a Result, this hides the indicator before the
     *   caller's chained handlers (e.g. `.onFailure { showErrorDialog() }`) run.
     *   The contract is: the indicator must be hidden before any post-block UI
     *   action (such as showing an error dialog) starts.
     * - `finally { isLoading = false }` is the **safety net for cancellation**
     *   (CancellationException thrown out of [block]). Not for the normal flow.
     *
     * Note: Do NOT chain onSuccess/onFailure inside [block] — callbacks would
     * execute before loading stops. Chain them on the returned Result instead.
     */
    suspend fun <T> runWithLoading(block: suspend () -> Result<T>): Result<T> {
        isLoading = true
        return try {
            block().also { isLoading = false }
        } finally {
            isLoading = false
        }
    }
}
