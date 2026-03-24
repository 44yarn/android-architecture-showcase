package io.github.yarn44.showcase.core.foundation.activity

import android.content.Context

/**
 * Abstraction for launching Activities from feature modules
 * without depending on concrete Activity classes.
 *
 * Feature modules depend on this interface (in core:foundation),
 * while the app module provides the implementation via DI.
 * This achieves Dependency Inversion between features.
 */
interface ActivityLauncher {
    fun launch(context: Context, target: ActivityTarget)
}

/**
 * Available Activity targets.
 * Each entry represents a launchable Activity without exposing its class.
 */
sealed class ActivityTarget {
    data object Info : ActivityTarget()
}
