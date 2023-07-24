package dev.cisnux.prime.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.cisnux.prime.presentation.utils.AppDestination

@Immutable
data class BottomNavigationItem(
    val icon: ImageVector,
    val title: String,
    val destination: AppDestination,
    val contentDescription: String,
)