package io.github.yarn44.showcase.feature.login

import io.github.yarn44.showcase.core.foundation.activity.ActivityTarget

/**
 * One-shot UI effects emitted by [LoginViewModel] via Channel + receiveAsFlow.
 *
 * - [NavigateToHome]: triggers navigation callback in the Composable layer.
 * - [LaunchActivity]: triggers ActivityLauncher in the Composable layer
 *   (Context is only available in UI, so the ViewModel cannot launch directly).
 */
sealed class LoginEffect {
    data class NavigateToHome(val displayName: String, val isGuest: Boolean) : LoginEffect()
    data class LaunchActivity(val target: ActivityTarget) : LoginEffect()
}
