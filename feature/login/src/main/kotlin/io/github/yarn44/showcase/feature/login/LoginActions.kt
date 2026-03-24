package io.github.yarn44.showcase.feature.login

/**
 * Callback container for LoginScreen user interactions.
 */
data class LoginActions(
    val onEmailChange: (email: String) -> Unit = {},
    val onPasswordChange: (password: String) -> Unit = {},
    val onRandomEmailClick: () -> Unit = {},
    val onTogglePasswordVisibility: () -> Unit = {},
    val onLoginClick: () -> Unit = {},
    val onLoginFailClick: () -> Unit = {},
    val onCancelClick: () -> Unit = {},
    val onInformationClick: () -> Unit = {},
)
