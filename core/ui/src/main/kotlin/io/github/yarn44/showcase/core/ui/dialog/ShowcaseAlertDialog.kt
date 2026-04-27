package io.github.yarn44.showcase.core.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Composable wrapper for [AlertDialog] driven by [DialogPresenter].
 *
 * Reads [DialogPresenter.dialogUiState] and renders the dialog.
 * Button clicks call [DialogPresenter.onDialogResult] to resume
 * the suspended coroutine.
 */
@Composable
fun ShowcaseAlertDialog(
    presenter: DialogPresenter,
) {
    val state = presenter.dialogUiState ?: return

    AlertDialog(
        onDismissRequest = { presenter.onDialogResult(DialogResult.Dismiss) },
        title = state.title?.let { { Text(it.value) } },
        text = state.message?.let { { Text(it.value) } },
        confirmButton = {
            state.positiveButton?.let { text ->
                TextButton(
                    onClick = { presenter.onDialogResult(DialogResult.Positive) },
                ) {
                    Text(text.value)
                }
            }
        },
        dismissButton = state.negativeButton?.let { text ->
            {
                TextButton(
                    onClick = { presenter.onDialogResult(DialogResult.Negative) },
                ) {
                    Text(text.value)
                }
            }
        },
    )
}
