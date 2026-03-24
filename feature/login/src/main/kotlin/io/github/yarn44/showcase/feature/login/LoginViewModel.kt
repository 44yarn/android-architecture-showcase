package io.github.yarn44.showcase.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.showcase.core.data.auth.AuthRepository
import io.github.yarn44.showcase.core.data.preferences.PreferenceKey
import io.github.yarn44.showcase.core.data.preferences.PreferenceStorage
import io.github.yarn44.showcase.core.foundation.activity.ActivityLauncher
import io.github.yarn44.showcase.core.foundation.activity.ActivityTarget
import io.github.yarn44.showcase.core.foundation.resulthandling.onFailureIgnoring
import io.github.yarn44.showcase.core.uikit.dialog.DialogPresenter
import io.github.yarn44.showcase.core.uikit.dialog.DialogUiState
import io.github.yarn44.showcase.core.uikit.indicator.IndicatorState
import io.github.yarn44.showcase.core.uikit.model.AdaptiveString
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceStorage: PreferenceStorage,
    val activityLauncher: ActivityLauncher,
    val dialogPresenter: DialogPresenter,
    val indicatorState: IndicatorState,
) : ViewModel() {

    private val _uiState = MutableLoginUiState()
    val uiState: LoginUiState = _uiState

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    val actions = LoginActions(
        onEmailChange = ::onEmailChange,
        onPasswordChange = ::onPasswordChange,
        onRandomEmailClick = ::onRandomEmailClick,
        onTogglePasswordVisibility = ::onTogglePasswordVisibility,
        onLoginClick = ::onLoginClick,
        onLoginFailClick = ::onLoginFailClick,
        onCancelClick = ::onCancelClick,
        onInformationClick = ::onInformationClick,
    )

    private var currentJob: Job? = null

    init {
        loadSavedEmail()
    }

    private fun onEmailChange(email: String) {
        _uiState.email = email
    }

    private fun onPasswordChange(password: String) {
        _uiState.password = password
    }

    private fun onRandomEmailClick() {
        _uiState.email = SAMPLE_EMAILS.random()
    }

    private fun onTogglePasswordVisibility() {
        _uiState.isPasswordVisible = _uiState.isPasswordVisible.not()
    }

    /**
     * Login flow demonstrates two patterns:
     * - Success: Navigate to Home (Effect pattern)
     * - Failure: Dialog with Actions DSL (Presenter suspend pattern),
     *   offering Guest Login or Cancel
     */
    private fun onLoginClick() {
        currentJob = viewModelScope.launch {
            indicatorState.runWithLoading {
                authRepository.login(_uiState.email, _uiState.password)
            }.onSuccess { result ->
                saveEmailIfEnabled()
                _effect.send(
                    LoginEffect.NavigateToHome(
                        displayName = result.displayName,
                        isGuest = false,
                    ),
                )
            }.onFailureIgnoring { exception ->
                showLoginErrorDialog(exception)
            }
        }
    }

    /**
     * Intentionally triggers a login failure for demo purposes.
     */
    private fun onLoginFailClick() {
        currentJob = viewModelScope.launch {
            indicatorState.runWithLoading {
                authRepository.login(_uiState.email, ERROR_PASSWORD)
            }.onFailureIgnoring { exception ->
                showLoginErrorDialog(exception)
            }
        }
    }

    private fun onCancelClick() {
        currentJob?.cancel()
        currentJob = null
    }

    /**
     * Information button: launch InfoActivity via Effect + ActivityLauncher.
     * Activity launch requires Context, so the Effect carries the target
     * and the Composable layer performs the actual launch.
     */
    private fun onInformationClick() {
        viewModelScope.launch {
            _effect.send(LoginEffect.LaunchActivity(ActivityTarget.Info))
        }
    }

    private fun loadSavedEmail() {
        viewModelScope.launch {
            val saveEnabled = preferenceStorage.getOrDefault(
                PreferenceKey.SaveEmailEnabled,
                default = false,
            )
            if (saveEnabled) {
                val savedEmail = preferenceStorage.getOrDefault(
                    PreferenceKey.SavedEmail,
                    default = "",
                )
                if (savedEmail.isNotEmpty()) {
                    _uiState.email = savedEmail
                }
            }
        }
    }

    /**
     * Error dialog with "Guest Login" / "Cancel" buttons.
     * Guest Login navigates to Home as a guest user.
     */
    private suspend fun showLoginErrorDialog(exception: Throwable) {
        dialogPresenter.requestDialogResult(
            uiState = DialogUiState(
                title = AdaptiveString("Login Failed"),
                message = AdaptiveString(
                    exception.message ?: "An unexpected error occurred",
                ),
                positiveButton = AdaptiveString("Guest Login"),
                negativeButton = AdaptiveString("Cancel"),
            ),
        ) {
            onPositiveButtonClick = {
                viewModelScope.launch {
                    _effect.send(
                        LoginEffect.NavigateToHome(
                            displayName = "Guest",
                            isGuest = true,
                        ),
                    )
                }
            }
        }
    }

    private suspend fun saveEmailIfEnabled() {
        val saveEnabled = preferenceStorage.getOrDefault(
            PreferenceKey.SaveEmailEnabled,
            default = false,
        )
        if (saveEnabled) {
            preferenceStorage.put(PreferenceKey.SavedEmail, _uiState.email)
        }
    }

    companion object {
        private const val ERROR_PASSWORD = "error_password"
        private val SAMPLE_EMAILS = listOf(
            "demo@example.com",
            "alice@showcase.dev",
            "bob@showcase.dev",
            "charlie@showcase.dev",
            "test@example.com",
        )
    }
}
