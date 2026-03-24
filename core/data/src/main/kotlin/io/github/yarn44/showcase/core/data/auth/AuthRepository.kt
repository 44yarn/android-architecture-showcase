package io.github.yarn44.showcase.core.data.auth

/**
 * Repository interface for authentication operations.
 */
interface AuthRepository {
    /** Attempts to log in with the given credentials. */
    suspend fun login(email: String, password: String): Result<AuthResult>

    companion object {
        /** Entering this password triggers an intentional error for demo purposes. */
        const val ERROR_PASSWORD = "error_password"
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
