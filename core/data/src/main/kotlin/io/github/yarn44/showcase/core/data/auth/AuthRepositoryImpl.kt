package io.github.yarn44.showcase.core.data.auth

import io.github.yarn44.showcase.core.foundation.resulthandling.logOnFailure
import io.github.yarn44.showcase.core.foundation.resulthandling.runCatchingCancellable
import javax.inject.Inject
import kotlinx.coroutines.delay

/**
 * Fake implementation with simulated network delays.
 */
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> =
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

    private companion object {
        const val SIMULATED_DELAY_MILLIS = 1500L
        /** Entering this password triggers an intentional error for demo purposes. */
        const val ERROR_PASSWORD = "error_password"
    }
}
