package io.github.yarn44.showcase.feature.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

/**
 * Standalone Activity launched via [ActivityLauncher].
 * Demonstrates loose coupling between feature modules —
 * the caller does not reference this class directly.
 */
@AndroidEntryPoint
class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            InfoScreen(onBackClick = ::finish)
        }
    }
}
