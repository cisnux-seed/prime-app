// compose utils
package dev.cisnux.prime.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import dev.cisnux.prime.presentation.navigation.BottomNavigationItem

const val SplashWaitTimeMillis = 1000L
val NavigationItems = listOf(
    BottomNavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        destination = AppDestination.HomeRoute,
        contentDescription = "home page"
    ),
    BottomNavigationItem(
        title = "Watchlist",
        icon = Icons.Default.Favorite,
        destination = AppDestination.WatchlistRoute,
        contentDescription = "watchlist page"
    ),
)