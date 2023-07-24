package dev.cisnux.prime.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED,
)
@Composable
private fun MovieCardTileShimmerPreview() {
    PrimeTheme {
        MovieCardTileShimmer()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCardTileShimmer(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier.shimmer(),
    ) {
        ElevatedCard(
            shape = MaterialTheme.shapes.extraSmall,
            onClick = {},
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(
                        start = (100 + 8).dp,
                        end = 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                content = {}
            )
        }
        MovieCardPlaceholder(
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.small)
                .size(height = 170.dp, width = 100.dp)
        )
    }
}