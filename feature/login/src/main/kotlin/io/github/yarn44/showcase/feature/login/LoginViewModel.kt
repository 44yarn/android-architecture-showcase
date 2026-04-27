package io.github.yarn44.showcase.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.showcase.core.data.auth.AuthException
import io.github.yarn44.showcase.core.data.auth.AuthRepository
import io.github.yarn44.showcase.core.data.preferences.PreferenceKey
import io.github.yarn44.showcase.core.data.preferences.PreferenceStorage
import io.github.yarn44.showcase.core.foundation.activity.ActivityLauncher
import io.github.yarn44.showcase.core.foundation.activity.ActivityTarget
import io.github.yarn44.showcase.core.foundation.adaptive.AdaptiveString
import io.github.yarn44.showcase.core.foundation.resulthandling.onFailureIgnoring
import io.github.yarn44.showcase.core.ui.dialog.DialogPresenter
import io.github.yarn44.showcase.core.ui.dialog.DialogUiState
import io.github.yarn44.showcase.core.ui.indicator.IndicatorState
import io.github.yarn44.showcase.feature.login.R
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

    private val _uiState = MutableLoginUiState(indicatorState)
    val uiState: LoginUiState = _uiState

    private val _effect = Channel<LoginEffect>()
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
        _uiState.password = CORRECT_PASSWORD
        currentJob = viewModelScope.launch {
            indicatorState.runWithLoading {
                authRepository.login(email = _uiState.email, password = _uiState.password)
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
        _uiState.password = AuthRepository.ERROR_PASSWORD
        currentJob = viewModelScope.launch {
            indicatorState.runWithLoading {
                authRepository.login(
                    email = _uiState.email,
                    password = _uiState.password,
                )
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
                PreferenceKey.Login.SaveEmailEnabled,
                default = true,
            )
            if (saveEnabled) {
                val savedEmail = preferenceStorage.getOrDefault(
                    PreferenceKey.Login.SavedEmail,
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
        val message = when (exception) {
            is AuthException -> AdaptiveString(R.string.login_invalid_credentials)
            else -> AdaptiveString("An unexpected error occurred. Please try again later.")
        }
        dialogPresenter.requestDialogResult(
            uiState = DialogUiState(
                title = AdaptiveString(R.string.login_failed),
                message = message,
                positiveButton = AdaptiveString(R.string.guest_login),
                negativeButton = AdaptiveString(R.string.cancel),
            ),
        ) {
            onPositiveButtonClick = {
                _effect.send(
                    LoginEffect.NavigateToHome(
                        displayName = "Guest",
                        isGuest = true,
                    ),
                )
            }
        }
    }

    private suspend fun saveEmailIfEnabled() {
        val saveEnabled = preferenceStorage.getOrDefault(
            PreferenceKey.Login.SaveEmailEnabled,
            default = true,
        )
        if (saveEnabled) {
            preferenceStorage.put(PreferenceKey.Login.SavedEmail, _uiState.email)
        }
    }

    private companion object {
        const val CORRECT_PASSWORD = "password"

        val SAMPLE_EMAILS = listOf(
            "demo@example.com",
            "alice@showcase.dev",
            "bob@showcase.dev",
            "charlie@showcase.dev",
            "test@example.com",
        )
    }
}
