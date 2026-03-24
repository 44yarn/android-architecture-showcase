package io.github.yarn44.showcase.activity

import android.content.Context
import android.content.Intent
import io.github.yarn44.showcase.core.foundation.activity.ActivityLauncher
import io.github.yarn44.showcase.core.foundation.activity.ActivityTarget
import io.github.yarn44.showcase.feature.info.InfoActivity
import javax.inject.Inject

/**
 * Concrete implementation that maps [ActivityTarget] to actual Activity classes.
 * Only the app module knows about concrete Activity classes from each feature.
 */
class ActivityLauncherImpl @Inject constructor() : ActivityLauncher {

    override fun launch(context: Context, target: ActivityTarget) {
        val intent = when (target) {
            is ActivityTarget.Info -> Intent(context, InfoActivity::class.java)
        }
        context.startActivity(intent)
    }
}
