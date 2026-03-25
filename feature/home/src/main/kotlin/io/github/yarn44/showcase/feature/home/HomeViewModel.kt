package io.github.yarn44.showcase.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.showcase.core.data.preferences.PreferenceKey
import io.github.yarn44.showcase.core.data.preferences.PreferenceStorage
import io.github.yarn44.showcase.core.uikit.model.AdaptiveString
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarPresenter
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarUiState
import io.github.yarn44.showcase.feature.home.R
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferenceStorage: PreferenceStorage,
    val snackbarPresenter: SnackbarPresenter,
) : ViewModel() {

    private val _uiState = MutableHomeUiState()
    val uiState: HomeUiState = _uiState

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    val actions = HomeActions(
        onBackPressed = ::onBackPressed,
        onSaveEmailToggle = ::onSaveEmailToggle,
        onLogoutClick = ::onLogoutClick,
    )

    init {
        val route = savedStateHandle.toRoute<HomeRoute>()
        _uiState.displayName = route.displayName
        _uiState.isGuest = route.isGuest

        loadPreferences()
        showWelcomeSnackbar(route.displayName, route.isGuest)
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _uiState.isSaveEmailEnabled = preferenceStorage.getOrDefault(
                PreferenceKey.Login.SaveEmailEnabled,
                default = false,
            )
            _uiState.savedEmail = preferenceStorage.getOrDefault(
                PreferenceKey.Login.SavedEmail,
                default = "",
            )
        }
    }

    private fun showWelcomeSnackbar(displayName: String, isGuest: Boolean) {
        val description = if (isGuest) {
            AdaptiveString(R.string.guest_mode)
        } else {
            AdaptiveString(R.string.welcome_message, displayName)
        }
        viewModelScope.launch {
            // Wait for the screen transition animation to finish before showing.
            delay(WELCOME_SNACKBAR_DELAY_MILLIS)
            snackbarPresenter.show(
                SnackbarUiState(description = description),
            )
        }
    }

    private fun onBackPressed() {
        snackbarPresenter.show(
            SnackbarUiState(description = AdaptiveString("Use the Logout button to sign out")),
        )
    }

    private fun onSaveEmailToggle(enabled: Boolean) {
        _uiState.isSaveEmailEnabled = enabled
        viewModelScope.launch {
            preferenceStorage.put(PreferenceKey.Login.SaveEmailEnabled, enabled)
            if (enabled.not()) {
                preferenceStorage.remove(PreferenceKey.Login.SavedEmail)
                _uiState.savedEmail = ""
            } else {
                _uiState.savedEmail = preferenceStorage.getOrDefault(
                    PreferenceKey.Login.SavedEmail,
                    default = "",
                )
            }
        }
    }

    private companion object {
        const val WELCOME_SNACKBAR_DELAY_MILLIS = 500L
    }

    private fun onLogoutClick() {
        viewModelScope.launch {
            _effect.send(HomeEffect.NavigateToLogin)
        }
    }
}
