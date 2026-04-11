package io.github.yarn44.showcase.feature.home

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.yarn44.showcase.core.uikit.model.AdaptiveString

/**
 * Read-only UI state for the home screen.
 */
@Stable
interface HomeUiState {
    val displayName: String
    val isGuest: Boolean
    val screenTitle: String
    val welcomeDescription: AdaptiveString
    val isSaveEmailEnabled: Boolean
    val savedEmail: String
}

/**
 * Mutable implementation of [HomeUiState].
 */
class MutableHomeUiState : HomeUiState {
    override var displayName: String by mutableStateOf("")
    override var isGuest: Boolean by mutableStateOf(false)
    override val screenTitle: String
        get() = if (isGuest) "Guest Home" else "Home"
    override val welcomeDescription: AdaptiveString
        get() = if (isGuest) {
            AdaptiveString(R.string.guest_mode)
        } else {
            AdaptiveString(R.string.welcome_message, displayName)
        }
    override var isSaveEmailEnabled: Boolean by mutableStateOf(true)
    override var savedEmail: String by mutableStateOf("")
}
