package io.github.yarn44.showcase.core.ui.dialog

import io.github.yarn44.showcase.core.foundation.adaptive.AdaptiveString

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
