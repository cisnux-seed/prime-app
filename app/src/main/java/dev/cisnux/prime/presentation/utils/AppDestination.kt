package dev.cisnux.prime.presentation.utils

import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import dev.cisnux.core.utils.FAVORITE_URI
import dev.cisnux.core.utils.HOME_URI

// compose utils
@Immutable
sealed class AppDestination(val route: String) {
    data object HomeRoute : AppDestination(route = "home") {
        val deepLinkPattern = "$HOME_URI/${HomeRoute.route}"
    }

    data object WatchlistRoute : AppDestination(route = "watchlist") {
        val deepLinkPattern = "$FAVORITE_URI/${WatchlistRoute.route}"
    }

    data object SplashRoute : AppDestination(route = "splash")
    data object MovieDetailRoute : AppDestination(route = "movies/{id}?isOutside={isOutside}") {
        val deepLinkPattern = "$HOME_URI/${MovieDetailRoute.route}"
        fun createDeepLink(id: Int, isOutside: Boolean = false) =
            "$HOME_URI/movies/$id?isOutside=$isOutside".toUri()
    }
}