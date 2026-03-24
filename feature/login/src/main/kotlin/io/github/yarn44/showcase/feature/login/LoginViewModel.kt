package io.github.yarn44.showcase.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.showcase.core.data.auth.AuthRepository
import io.github.yarn44.showcase.core.data.preferences.PreferenceKey
import io.github.yarn44.showcase.core.data.preferences.PreferenceStorage
import io.github.yarn44.showcase.core.foundation.resulthandling.onFailureIgnoring
import io.github.yarn44.showcase.core.uikit.dialog.DialogPresenter
import io.github.yarn44.showcase.core.uikit.dialog.DialogUiState
import io.github.yarn44.showcase.core.uikit.indicator.IndicatorState
import io.github.yarn44.showcase.core.uikit.model.AdaptiveString
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarPresenter
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarUiState
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceStorage: PreferenceStorage,
    val snackbarPresenter: SnackbarPresenter,
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
        onLoginClick = ::onLoginClick,
        onCancelClick = ::onCancelClick,
        onSaveEmailToggle = ::onSaveEmailToggle,
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

    /**
     * Login flow demonstrates two patterns:
     * - Success: Toast (Effect pattern, fire-and-forget)
     * - Failure: Dialog with Actions DSL (Presenter suspend pattern)
     */
    private fun onLoginClick() {
        currentJob = viewModelScope.launch {
            indicatorState.runWithLoading {
                authRepository.login(_uiState.email, _uiState.password)
            }.onSuccess { result ->
                _uiState.lastLoginName = result.displayName
                _effect.send(LoginEffect.ShowToast("Welcome, ${result.displayName}!"))
                saveEmailIfEnabled()
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
     * Save email toggle: Snackbar (Presenter direct call).
     */
    private fun onSaveEmailToggle(enabled: Boolean) {
        _uiState.isSaveEmailEnabled = enabled
        viewModelScope.launch {
            preferenceStorage.put(PreferenceKey.SaveEmailEnabled, enabled)
            if (enabled) {
                saveEmailIfEnabled()
                snackbarPresenter.show(
                    SnackbarUiState(description = AdaptiveString("Email saved")),
                )
            } else {
                preferenceStorage.remove(PreferenceKey.SavedEmail)
                snackbarPresenter.show(
                    SnackbarUiState(description = AdaptiveString("Email cleared")),
                )
            }
        }
    }

    private fun loadSavedEmail() {
        viewModelScope.launch {
            val saveEnabled = preferenceStorage.getOrDefault(
                PreferenceKey.SaveEmailEnabled,
                default = false,
            )
            val savedEmail = preferenceStorage.getOrDefault(
                PreferenceKey.SavedEmail,
                default = "",
            )
            _uiState.email = savedEmail
            _uiState.isSaveEmailEnabled = saveEnabled
        }
    }

    private suspend fun showLoginErrorDialog(exception: Throwable) {
        dialogPresenter.requestDialogResult(
            uiState = DialogUiState(
                title = AdaptiveString("Login Failed"),
                message = AdaptiveString(
                    exception.message ?: "An unexpected error occurred",
                ),
                positiveButton = AdaptiveString("Retry"),
                negativeButton = AdaptiveString("Cancel"),
            ),
        ) {
            onPositiveButtonClick = { onLoginClick() }
        }
    }

    private suspend fun saveEmailIfEnabled() {
        if (_uiState.isSaveEmailEnabled) {
            preferenceStorage.put(PreferenceKey.SavedEmail, _uiState.email)
        }
    }
}
