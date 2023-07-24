package dev.cisnux.prime.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.cisnux.prime.presentation.detail.MovieDetailScreen
import dev.cisnux.prime.presentation.home.HomeScreen
import dev.cisnux.prime.presentation.splash.SplashScreen
import dev.cisnux.prime.presentation.utils.AppDestination

@Composable
fun PrimeAppNavGraph(
    navController: NavHostController = rememberNavController(),
    navComponentAction: NavComponentAction = rememberNavComponentAction(
        navController = navController,
        intentAction = rememberIntentAction()
    ),
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.SplashRoute.route
    ) {
        composable(
            route = AppDestination.SplashRoute.route,
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
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) {
            SplashScreen(navigateToHome = navComponentAction.navigateToHomeForSplash)
        }
        composable(
            route = AppDestination.HomeRoute.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = AppDestination.HomeRoute.deepLinkPattern
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
            HomeScreen(
                navigateToMovieDetail = navComponentAction.navigateToMovieDetail,
                navigateForBottomNav = navComponentAction.bottomNavigation
            )
        }
        composable(
            route = AppDestination.MovieDetailRoute.route,
            arguments = listOf(
                navArgument(name = "id") {
                    nullable = false
                    type = NavType.IntType
                },
                navArgument(name = "isOutside") {
                    nullable = false
                    defaultValue = false
                    type = NavType.BoolType
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = AppDestination.MovieDetailRoute.deepLinkPattern
            }),
            enterTransition = {
                expandIn(
                    expandFrom = Alignment.CenterStart,
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
        ) { backStackEntry ->
            val isOutside = backStackEntry.arguments!!.getBoolean("isOutside")
            MovieDetailScreen(
                navigateUp = if (!isOutside) navComponentAction.navigateUp
                else navComponentAction.navigateToHomeForDynamicModule,
                navigateToMovieDetail = navComponentAction.navigateToMovieDetail,
            )
        }
    }
}