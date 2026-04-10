package io.github.yarn44.showcase.core.data.auth

import io.github.yarn44.showcase.core.foundation.resulthandling.logOnFailure
import io.github.yarn44.showcase.core.foundation.resulthandling.runCatchingCancellable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

/**
 * Repository for authentication operations.
 * Fake implementation with simulated network delays.
 */
@Singleton
class AuthRepository @Inject constructor() {

    /** Attempts to log in with the given credentials. */
    suspend fun login(email: String, password: String): Result<AuthResult> =
        runCatchingCancellable {
            delay(SIMULATED_DELAY_MILLIS)
            if (password == ERROR_PASSWORD) {
                throw AuthException("Authentication failed: invalid credentials")
            }
            AuthResult(
                token = "fake-token-${System.currentTimeMillis()}",
                displayName = email.substringBefore("@"),
            )
        }.logOnFailure()

    companion object {
        /** Public for demo UI (LoginViewModel uses this to trigger an intentional login failure). */
        const val ERROR_PASSWORD = "error"

        private const val SIMULATED_DELAY_MILLIS = 1500L
    }
}

/**
 * Represents a successful authentication result.
 */
data class AuthResult(
    val token: String,
    val displayName: String,
)

/**
 * Sample-only exception for showcasing how the UI layer can map error types
 * to user-facing text (see `LoginViewModel.showLoginErrorDialog`).
 *
 * IMPORTANT — design note for real projects:
 *
 * This showcase intentionally defines a single dedicated exception class so
 * that a reader can see the "type-based error -> UI text mapping" pattern.
 * In real projects you should **avoid proliferating custom exception types**:
 * typically one reportable exception (for crash reporting) is enough, and
 * rich failure information should be modeled as data (e.g. sealed result
 * types) rather than as a deep exception hierarchy.
 *
 * @param message Diagnostic message for logging, not for UI display.
 *     UI-facing text must be chosen by the UI layer (see `AdaptiveString`).
 */
class AuthException(message: String) : RuntimeException(message)
