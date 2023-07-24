package dev.cisnux.prime.presentation.navigation

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.glance.action.Action
import androidx.glance.appwidget.action.actionStartActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.cisnux.prime.presentation.utils.AppDestination
import kotlin.reflect.KClass

class NavComponentAction(
    navController: NavHostController,
    val intentAction: IntentAction,
) {
    val navigateToMovieDetail: (
        id: Int,
    ) -> Unit = { id ->
        navController.navigate(route = "movies/$id")
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
    val navigateToHomeForSplash: () -> Unit = {
        navController.navigate(AppDestination.HomeRoute.route) {
            popUpTo(AppDestination.SplashRoute.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val navigateToHomeForDynamicModule: () -> Unit = {
        navController.navigate(AppDestination.HomeRoute.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val bottomNavigation: (destination: AppDestination, currentRoute: AppDestination) -> Unit =
        { destination, currentRoute ->
            if (destination.route != currentRoute.route)
                when (destination) {
                    AppDestination.HomeRoute -> {
                        intentAction.navigateToHomeByDeepLink()
                    }

                    AppDestination.WatchlistRoute -> {
                        intentAction.navigateToWatchlistByIntent()
                    }

                    else -> {
                        navController.navigate(route = destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                }
        }
}

class IntentAction(context: Context) {
    val navigateToMovieDetailByPendingIntent: (id: Int, isOutside: Boolean, cls: KClass<*>) -> Unit =
        { id, isOutside, cls ->
            val requestCode = 239
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                AppDestination.MovieDetailRoute
                    .createDeepLink(id = id, isOutside = isOutside),
                context,
                cls.java
            )
            val deepLinkPendingIntent: PendingIntent? =
                TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(deepLinkIntent)
                    getPendingIntent(
                        requestCode,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                        else PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
            deepLinkPendingIntent?.send()
        }

    val navigateToMovieDetailForWidget: (id: Int, isOutside: Boolean, cls: KClass<*>) -> Action =
        { id, isOutside, cls ->
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                AppDestination.MovieDetailRoute
                    .createDeepLink(id = id, isOutside = isOutside),
                context,
                cls.java
            )
            actionStartActivity(intent = deepLinkIntent)
        }

    val navigateToWatchlistByIntent = {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            data = AppDestination.WatchlistRoute.deepLinkPattern.toUri()
        }
        context.startActivity(deepLinkIntent)
    }

    val navigateToHomeByDeepLink = {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            data = AppDestination.HomeRoute.deepLinkPattern.toUri()
        }
        context.startActivity(deepLinkIntent)
    }
}

@Composable
fun rememberNavComponentAction(
    navController: NavHostController,
    intentAction: IntentAction,
): NavComponentAction = remember(navController, intentAction) {
    NavComponentAction(navController = navController, intentAction = intentAction)
}

@Composable
fun rememberIntentAction(
    context: Context = LocalContext.current
): IntentAction = remember(context) {
    IntentAction(context = context)
}
