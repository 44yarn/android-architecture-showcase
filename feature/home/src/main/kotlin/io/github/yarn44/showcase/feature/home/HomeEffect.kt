package io.github.yarn44.showcase.feature.home

/**
 * One-shot events from HomeViewModel to the UI layer.
 */
sealed class HomeEffect {
    data object NavigateToLogin : HomeEffect()
}
