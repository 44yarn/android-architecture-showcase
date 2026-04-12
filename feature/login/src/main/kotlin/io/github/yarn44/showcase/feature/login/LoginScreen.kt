package io.github.yarn44.showcase.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.yarn44.showcase.core.foundation.lifecycle.CollectAsEffect
import io.github.yarn44.showcase.core.uikit.dialog.ShowcaseAlertDialog
import io.github.yarn44.showcase.core.uikit.indicator.IndicatorState

@Composable
fun LoginScreen(
    onNavigateToHome: (displayName: String, isGuest: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // Effect pattern: lifecycle-aware collect for one-shot events
    viewModel.effect.CollectAsEffect { effect ->
        when (effect) {
            is LoginEffect.NavigateToHome -> {
                onNavigateToHome(effect.displayName, effect.isGuest)
            }
            is LoginEffect.LaunchActivity -> {
                viewModel.activityLauncher.launch(context, effect.target)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LoginContent(
            uiState = viewModel.uiState,
            actions = viewModel.actions,
        )

        if (viewModel.indicatorState.isLoading) {
            CircularProgressIndicator()
        }
    }

    // Presenter pattern: Dialog rendering
    ShowcaseAlertDialog(presenter = viewModel.dialogPresenter)
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    actions: LoginActions,
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
                text = "Android Showcase",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = actions.onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = actions.onRandomEmailClick) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Random email",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = actions.onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = uiState.passwordVisualTransformation,
                trailingIcon = {
                    IconButton(onClick = actions.onTogglePasswordVisibility) {
                        Icon(
                            imageVector = uiState.passwordToggleIcon,
                            contentDescription = uiState.passwordToggleContentDescription,
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = actions.onLoginClick,
                enabled = uiState.isLoginEnabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Login")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = actions.onLoginFailClick,
                    enabled = uiState.isButtonsEnabled,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Login (Fail)")
                }

                OutlinedButton(
                    onClick = actions.onCancelClick,
                    enabled = uiState.isButtonsEnabled.not(),
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancel")
                }
            }

            TextButton(
                onClick = actions.onInformationClick,
                enabled = uiState.isButtonsEnabled,
            ) {
                Text("Information")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    MaterialTheme {
        LoginContent(
            uiState = MutableLoginUiState(
                indicatorState = IndicatorState(),
            ),
            actions = LoginActions(),
        )
    }
}
