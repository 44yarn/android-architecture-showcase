package io.github.yarn44.showcase.feature.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.yarn44.showcase.core.ui.indicator.IndicatorState

/**
 * Read-only UI state for the login screen.
 */
@Stable
interface LoginUiState {
    val email: String
    val password: String
    val isPasswordVisible: Boolean
    val passwordVisualTransformation: VisualTransformation
    val passwordToggleIcon: ImageVector
    val passwordToggleContentDescription: String
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
    override val passwordVisualTransformation: VisualTransformation
        get() = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    override val passwordToggleIcon: ImageVector
        get() = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
    override val passwordToggleContentDescription: String
        get() = if (isPasswordVisible) "Hide password" else "Show password"
    override val isButtonsEnabled: Boolean
        get() = indicatorState.isLoading.not()
    override val isLoginEnabled: Boolean
        get() = isButtonsEnabled && email.isNotBlank() && password.isNotBlank()
}
