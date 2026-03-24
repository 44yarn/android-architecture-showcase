package io.github.yarn44.showcase.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import io.github.yarn44.showcase.navigation.ShowcaseNavGraph

@Composable
fun ShowcaseApp() {
    val navController = rememberNavController()
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ShowcaseNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
