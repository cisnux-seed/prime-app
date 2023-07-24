package dev.cisnux.favorite.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import dev.cisnux.favorite.presentation.watchlist.WatchlistMovieScreen
import dev.cisnux.prime.presentation.MainActivity
import dev.cisnux.prime.presentation.navigation.NavComponentAction
import dev.cisnux.prime.presentation.navigation.rememberIntentAction
import dev.cisnux.prime.presentation.navigation.rememberNavComponentAction
import dev.cisnux.prime.presentation.utils.AppDestination

@Composable
fun WatchlistMovieNavGraph(
    navController: NavHostController = rememberNavController(),
    navComponentAction: NavComponentAction = rememberNavComponentAction(
        navController = navController,
        intentAction = rememberIntentAction()
    ),
    factory: ViewModelProvider.Factory,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.WatchlistRoute.route
    ) {
        composable(
            route = AppDestination.WatchlistRoute.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = AppDestination.WatchlistRoute.deepLinkPattern
            }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) {
            WatchlistMovieScreen(
                navigateToMovieDetail = { id ->
                    navComponentAction.intentAction.navigateToMovieDetailByPendingIntent(
                        id,
                        true,
                        MainActivity::class
                    )
                },
                navigateForBottomNav = navComponentAction.bottomNavigation,
                watchlistMovieViewModel = viewModel(factory = factory)
            )
        }
    }
}