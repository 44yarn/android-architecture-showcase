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
        /** Entering this password triggers an intentional error for demo purposes. */
        const val ERROR_PASSWORD = "error_password"

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
 * @param message Diagnostic message for logging, not for UI display.
 */
class AuthException(message: String) : RuntimeException(message)
