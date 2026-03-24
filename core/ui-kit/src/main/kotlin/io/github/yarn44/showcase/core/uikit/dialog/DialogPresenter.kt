package io.github.yarn44.showcase.core.uikit.dialog

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Dialog presenter using suspendCancellableCoroutine.
 *
 * Requests a dialog and suspends until the user responds.
 * The UI layer reads [dialogUiState] to render the dialog,
 * then calls [onDialogResult] to resume the suspended coroutine.
 */
@Stable
class DialogPresenter @Inject constructor() {

    var dialogUiState: DialogUiState? by mutableStateOf(null)
        private set

    private var currentContinuation: CancellableContinuation<DialogResult>? = null

    /**
     * Requests a dialog and suspends until the user responds.
     *
     * @param uiState dialog display content
     * @return the user's choice as [DialogResult]
     */
    suspend fun requestDialogResult(uiState: DialogUiState): DialogResult =
        suspendCancellableCoroutine { continuation ->
            currentContinuation?.cancel()

            currentContinuation = continuation
            dialogUiState = uiState

            continuation.invokeOnCancellation {
                dialogUiState = null
                currentContinuation = null
            }
        }

    /**
     * Requests a dialog and executes the corresponding action based on the result.
     *
     * @param uiState dialog display content
     * @param actions DSL to define actions for each result
     */
    suspend fun requestDialogResult(
        uiState: DialogUiState,
        actions: DialogActions.() -> Unit,
    ) {
        val dialogActions = DialogActions().apply(actions)
        val result = requestDialogResult(uiState)
        when (result) {
            is DialogResult.Positive -> dialogActions.onPositiveButtonClick?.invoke()
            is DialogResult.Negative -> dialogActions.onNegativeButtonClick?.invoke()
            is DialogResult.Dismiss -> dialogActions.onDismissRequest?.invoke()
        }
    }

    /**
     * Called from the UI layer when the user responds to the dialog.
     * Resumes the suspended [requestDialogResult] coroutine.
     */
    fun onDialogResult(result: DialogResult) {
        currentContinuation?.resume(result)
        currentContinuation = null
        dialogUiState = null
    }

    /**
     * DSL container for dialog result actions.
     */
    class DialogActions {
        var onPositiveButtonClick: (suspend () -> Unit)? = null
        var onNegativeButtonClick: (suspend () -> Unit)? = null
        var onDismissRequest: (suspend () -> Unit)? = null
    }
}
