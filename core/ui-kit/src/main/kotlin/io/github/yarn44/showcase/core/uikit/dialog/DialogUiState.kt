package io.github.yarn44.showcase.core.uikit.dialog

import io.github.yarn44.showcase.core.uikit.model.AdaptiveString

/**
 * Pure UI state for an alert dialog. Contains only display data.
 * User interaction results are handled via [DialogResult].
 */
data class DialogUiState(
    val title: AdaptiveString? = null,
    val message: AdaptiveString? = null,
    val positiveButton: AdaptiveString? = null,
    val negativeButton: AdaptiveString? = null,
)
