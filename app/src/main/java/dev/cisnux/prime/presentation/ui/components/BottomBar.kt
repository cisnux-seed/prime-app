package dev.cisnux.prime.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import dev.cisnux.prime.presentation.navigation.BottomNavigationItem
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import dev.cisnux.prime.presentation.utils.AppDestination
import dev.cisnux.prime.presentation.utils.NavigationItems

@Preview
@Composable
private fun BottomBarPreview() {
    PrimeTheme {
        BottomBar(
            currentRoute = AppDestination.HomeRoute,
            onSelectedDestination = { _, _ -> },
            navigationItems = NavigationItems
        )
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentRoute: AppDestination,
    navigationItems: List<BottomNavigationItem>,
    onSelectedDestination: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.clearAndSetSemantics {})
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                selected = currentRoute.route == item.destination.route,
                onClick = {
                    onSelectedDestination(item.destination, currentRoute)
                },
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = item.contentDescription
                }
            )
        }
    }
}