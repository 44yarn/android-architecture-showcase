package io.github.yarn44.showcase

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.yarn44.showcase.navigation.ShowcaseNavGraph

@Composable
fun ShowcaseApp() {
    val navController = rememberNavController()
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ShowcaseNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
