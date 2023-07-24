package dev.cisnux.prime.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.cisnux.core.utils.ORIGINAL_IMAGE_URL
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
)
@Composable
private fun MovieCardPreview() {
    PrimeTheme {
        MovieCard(
            title = "Spider-Man: Across the Spider-Verse",
            poster = "$ORIGINAL_IMAGE_URL/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
            onClick = {}
        )
    }
}

@Composable
fun MovieCard(
    title: String,
    poster: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = poster,
        error = {
            MovieCardPlaceholder(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .size(height = 220.dp, width = 120.dp)
            )
        },
        loading = {
            MovieCardPlaceholder(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .size(height = 220.dp, width = 120.dp)
            )
        },
        contentScale = ContentScale.FillBounds,
        contentDescription = title,
        modifier = modifier
            .testTag("movieCard")
            .size(height = 220.dp, width = 120.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
    )
}