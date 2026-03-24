package io.github.yarn44.showcase.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.showcase.core.data.preferences.PreferenceKey
import io.github.yarn44.showcase.core.data.preferences.PreferenceStorage
import io.github.yarn44.showcase.core.uikit.model.AdaptiveString
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarPresenter
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarUiState
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
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
        onSaveEmailToggle = ::onSaveEmailToggle,
        onLogoutClick = ::onLogoutClick,
    )

    init {
        val displayName = savedStateHandle.get<String>("displayName") ?: ""
        val isGuest = savedStateHandle.get<Boolean>("isGuest") ?: false
        _uiState.displayName = displayName
        _uiState.isGuest = isGuest

        loadPreferences()
        showWelcomeSnackbar(displayName, isGuest)
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _uiState.isSaveEmailEnabled = preferenceStorage.getOrDefault(
                PreferenceKey.SaveEmailEnabled,
                default = false,
            )
            _uiState.savedEmail = preferenceStorage.getOrDefault(
                PreferenceKey.SavedEmail,
                default = "",
            )
        }
    }

    private fun showWelcomeSnackbar(displayName: String, isGuest: Boolean) {
        val message = if (isGuest) "Guest mode" else "Welcome, $displayName!"
        snackbarPresenter.show(
            SnackbarUiState(description = AdaptiveString(message)),
        )
    }

    private fun onSaveEmailToggle(enabled: Boolean) {
        _uiState.isSaveEmailEnabled = enabled
        viewModelScope.launch {
            preferenceStorage.put(PreferenceKey.SaveEmailEnabled, enabled)
            if (enabled.not()) {
                preferenceStorage.remove(PreferenceKey.SavedEmail)
                _uiState.savedEmail = ""
            } else {
                _uiState.savedEmail = preferenceStorage.getOrDefault(
                    PreferenceKey.SavedEmail,
                    default = "",
                )
            }
        }
    }

    private fun onLogoutClick() {
        viewModelScope.launch {
            _effect.send(HomeEffect.NavigateToLogin)
        }
    }
}
