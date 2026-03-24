package io.github.yarn44.showcase.feature.login

/**
 * One-shot UI effects emitted by [LoginViewModel] via Channel + receiveAsFlow.
 * Collected in the Composable layer to trigger UI-only actions (e.g. Toast).
 *
 * For suspend-based UI interactions (e.g. Dialog that waits for user response),
 * use Presenter pattern instead — see [DialogPresenter].
 */
sealed class LoginEffect {
    data class ShowToast(val message: String) : LoginEffect()
}
