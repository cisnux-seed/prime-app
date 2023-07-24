package dev.cisnux.prime.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
private fun MovieCardTilePreview() {
    PrimeTheme {
        MovieCardTile(
            title = "Spider-Man: Across the Spider-Verse",
            overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters the Spider Society, a team of Spider-People charged with protecting the Multiverse’s very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must set out on his own to save those he loves most.",
            poster = "$ORIGINAL_IMAGE_URL/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
            onClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCardTile(
    title: String,
    overview: String,
    poster: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier,
    ) {
        ElevatedCard(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = onClick,
        ){
            Column(
                modifier = Modifier.padding(
                    start = (100 + 8).dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        SubcomposeAsyncImage(
            model = poster,
            error = {
                MovieCardPlaceholder(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .size(height = 170.dp, width = 100.dp)
                )
            },
            loading = {
                MovieCardPlaceholder(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .size(height = 170.dp, width = 100.dp)
                )
            },
            contentScale = ContentScale.FillBounds,
            contentDescription = title,
            modifier = Modifier
                .size(height = 170.dp, width = 100.dp)
                .padding(start = 8.dp, bottom = 8.dp)
                .clip(shape = MaterialTheme.shapes.small)
        )
    }
}
