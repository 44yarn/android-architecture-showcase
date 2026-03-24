package io.github.yarn44.showcase.core.foundation.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Composable destination with a default horizontal slide transition.
 * Wraps [composable] to apply consistent enter/exit animations across all screens.
 */
inline fun <reified T : Any> NavGraphBuilder.screen(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 400,
        easing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
    ),
    noinline content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        enterTransition = {
            slideInHorizontally(animationSpec = animationSpec) { it }
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = animationSpec) { -it }
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = animationSpec) { -it }
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = animationSpec) { it }
        },
        content = content,
    )
}
