package io.github.yarn44.showcase.core.uikit.snackbar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

/**
 * Snackbar state manager.
 * Call [show] to display a snackbar and [hide] to dismiss it.
 */
@Stable
class SnackbarPresenter @Inject constructor() {

    var uiState: SnackbarUiState? by mutableStateOf(null)
        private set

    val isVisible: Boolean
        get() = uiState != null

    fun show(snackbar: SnackbarUiState) {
        uiState = snackbar
    }

    fun hide() {
        uiState = null
    }
}
