package io.github.yarn44.showcase.feature.home

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Read-only UI state for the home screen.
 */
@Stable
interface HomeUiState {
    val displayName: String
    val isGuest: Boolean
    val screenTitle: String
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
    override var isSaveEmailEnabled: Boolean by mutableStateOf(false)
    override var savedEmail: String by mutableStateOf("")
}
