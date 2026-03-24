package io.github.yarn44.showcase.feature.login

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.yarn44.showcase.core.uikit.indicator.IndicatorState

/**
 * Read-only UI state for the login screen.
 */
@Stable
interface LoginUiState {
    val email: String
    val password: String
    val isPasswordVisible: Boolean
    val isLoginEnabled: Boolean
    val isButtonsEnabled: Boolean
}

/**
 * Mutable implementation of [LoginUiState].
 * Each property is backed by [mutableStateOf] for Compose snapshot integration.
 */
class MutableLoginUiState(
    private val indicatorState: IndicatorState,
) : LoginUiState {
    override var email: String by mutableStateOf("demo@example.com")
    override var password: String by mutableStateOf("password")
    override var isPasswordVisible: Boolean by mutableStateOf(false)
    override val isButtonsEnabled: Boolean
        get() = indicatorState.isLoading.not()
    override val isLoginEnabled: Boolean
        get() = isButtonsEnabled && email.isNotBlank() && password.isNotBlank()
}
