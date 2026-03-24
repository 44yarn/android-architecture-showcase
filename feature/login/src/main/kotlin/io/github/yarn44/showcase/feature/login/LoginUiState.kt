package io.github.yarn44.showcase.feature.login

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Read-only UI state for the login screen.
 */
@Stable
interface LoginUiState {
    val email: String
    val password: String
    val isPasswordVisible: Boolean
    val isLoginEnabled: Boolean
}

/**
 * Mutable implementation of [LoginUiState].
 * Each property is backed by [mutableStateOf] for Compose snapshot integration.
 */
class MutableLoginUiState : LoginUiState {
    override var email: String by mutableStateOf("demo@example.com")
    override var password: String by mutableStateOf("password")
    override var isPasswordVisible: Boolean by mutableStateOf(false)
    override val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}
