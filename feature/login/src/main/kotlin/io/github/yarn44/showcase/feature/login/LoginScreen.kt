package io.github.yarn44.showcase.feature.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.yarn44.showcase.core.uikit.dialog.ShowcaseAlertDialog
import io.github.yarn44.showcase.core.uikit.snackbar.SnackbarView

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // Effect pattern: collect one-shot events (Toast)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LoginContent(
            uiState = viewModel.uiState,
            isLoading = { viewModel.indicatorState.isLoading },
            actions = viewModel.actions,
        )

        SnackbarView(presenter = viewModel.snackbarPresenter)
    }

    // Presenter pattern: Dialog rendering
    ShowcaseAlertDialog(presenter = viewModel.dialogPresenter)
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    isLoading: () -> Boolean,
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
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = actions.onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )

            if (uiState.lastLoginName.isNotEmpty()) {
                Text(
                    text = "Last login: ${uiState.lastLoginName}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Button(
                onClick = actions.onLoginClick,
                enabled = uiState.isLoginEnabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Login")
            }

            OutlinedButton(
                onClick = actions.onCancelClick,
                enabled = isLoading(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Cancel")
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
        }

        if (isLoading()) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    MaterialTheme {
        LoginContent(
            uiState = MutableLoginUiState(),
            isLoading = { false },
            actions = LoginActions(),
        )
    }
}
