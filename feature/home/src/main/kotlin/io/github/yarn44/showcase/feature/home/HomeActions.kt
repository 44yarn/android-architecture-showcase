package io.github.yarn44.showcase.feature.home

/**
 * Callback container for HomeScreen user interactions.
 */
data class HomeActions(
    val onSaveEmailToggle: (enabled: Boolean) -> Unit = {},
    val onLogoutClick: () -> Unit = {},
)
