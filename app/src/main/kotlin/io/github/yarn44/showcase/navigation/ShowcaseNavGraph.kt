package io.github.yarn44.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.github.yarn44.showcase.core.foundation.navigation.screen
import io.github.yarn44.showcase.feature.home.HomeScreen
import io.github.yarn44.showcase.feature.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
data class HomeRoute(
    val displayName: String,
    val isGuest: Boolean,
)

@Composable
fun ShowcaseNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier,
    ) {
        screen<LoginRoute> {
            LoginScreen(
                onNavigateToHome = { displayName, isGuest ->
                    navController.navigate(HomeRoute(displayName, isGuest))
                },
            )
        }
        screen<HomeRoute> {
            HomeScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }
    }
}
