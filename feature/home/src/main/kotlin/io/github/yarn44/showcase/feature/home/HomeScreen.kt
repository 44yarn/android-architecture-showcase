package io.github.yarn44.showcase.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarView

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    // Disable system back — guide the user to the Logout button instead.
    BackHandler(onBack = viewModel.actions.onBackPressed)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        HomeContent(
            uiState = viewModel.uiState,
            actions = viewModel.actions,
        )

        SnackbarView(presenter = viewModel.snackbarPresenter)
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = if (uiState.isGuest) "Guest Home" else "Home",
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                text = "Hello, ${uiState.displayName}!",
                style = MaterialTheme.typography.bodyLarge,
            )

            if (uiState.savedEmail.isNotEmpty()) {
                Text(
                    text = "Saved email: ${uiState.savedEmail}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Remember Email")
                Switch(
                    checked = uiState.isSaveEmailEnabled,
                    onCheckedChange = actions.onSaveEmailToggle,
                )
            }

            OutlinedButton(
                onClick = actions.onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    MaterialTheme {
        HomeContent(
            uiState = MutableHomeUiState().apply {
                displayName = "John"
                savedEmail = "john@example.com"
            },
            actions = HomeActions(),
        )
    }
}
