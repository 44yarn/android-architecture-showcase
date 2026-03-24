package io.github.yarn44.showcase.feature.home

import kotlinx.serialization.Serializable

/** Type-safe Navigation route for the home screen. */
@Serializable
data class HomeRoute(
    val displayName: String,
    val isGuest: Boolean,
)
