package io.github.yarn44.showcase.core.ui.dialog

/**
 * Represents the user's response to a dialog.
 *
 * - [Positive]: confirmButton — affirmative action
 * - [Negative]: dismissButton — explicit rejection
 * - [Dismiss]: onDismissRequest — background tap or back press
 */
sealed class DialogResult {
    data object Positive : DialogResult()
    data object Negative : DialogResult()
    data object Dismiss : DialogResult()
}
