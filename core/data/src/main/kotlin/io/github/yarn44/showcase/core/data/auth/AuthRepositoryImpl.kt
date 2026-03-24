package io.github.yarn44.showcase.core.data.auth

import io.github.yarn44.showcase.core.foundation.resulthandling.runCatchingCancellable
import io.github.yarn44.showcase.core.foundation.resulthandling.withErrorLog
import kotlinx.coroutines.delay
import javax.inject.Inject

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
        }.withErrorLog()

    private companion object {
        const val SIMULATED_DELAY_MILLIS = 1500L
        /** Entering this password triggers an intentional error for demo purposes. */
        const val ERROR_PASSWORD = "error"
    }
}
