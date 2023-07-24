package dev.cisnux.prime.presentation.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.cisnux.prime.presentation.ui.theme.md_theme_placeholder

@Composable
fun MovieCardPlaceholder(modifier: Modifier = Modifier) {
    Surface(
        color = md_theme_placeholder,
        content = {},
        modifier = modifier.testTag("movieCardPlaceholder"),
    )
}