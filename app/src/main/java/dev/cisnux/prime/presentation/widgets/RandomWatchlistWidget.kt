package dev.cisnux.prime.presentation.widgets

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.glance.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import dagger.hilt.EntryPoint
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dev.cisnux.core.domain.usecases.MovieInteractor
import androidx.glance.text.FontFamily
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.MainActivity
import dev.cisnux.prime.presentation.navigation.rememberIntentAction
import dev.cisnux.prime.presentation.ui.theme.PrimeGlanceTheme

class RandomWatchlistWidget : GlanceAppWidget() {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RandomWatchlistEntryPoint {
        fun movieUseCase(): MovieInteractor
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            RandomWatchlistEntryPoint::class.java
        )

        provideContent {
            val movieUseCase = remember { hiltEntryPoint.movieUseCase() }
            val randomWatchlistMovie by movieUseCase.getRandomWatchlistForWidget()
                .collectAsState(initial = null)
            val intentAction = rememberIntentAction(context = context)

            PrimeGlanceTheme {
                randomWatchlistMovie?.let {
                    val action = intentAction.navigateToMovieDetailForWidget(
                        it.id,
                        true,
                        MainActivity::class
                    )

                    MovieCardTile(
                        title = it.title,
                        overview = it.overview,
                        poster = it.poster,
                        onClick = action,
                    )
                } ?: EmptyWatchlist(context = context)
            }
        }
    }

    @Composable
    private fun EmptyWatchlist(
        context: Context,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Image(
                provider = ImageProvider(resId = R.drawable.empty_watchlist),
                contentDescription = context.getString(R.string.no_watchlist),
                modifier = GlanceModifier.size(width = 150.dp, height = 150.dp)
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = context.getString(R.string.no_watchlist),
            )
        }
    }

    @Composable
    private fun MovieCardTile(
        title: String,
        overview: String,
        poster: Bitmap,
        onClick: Action,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = modifier
        ) {
            Box(
                modifier = GlanceModifier.background(GlanceTheme.colors.surfaceVariant)
                    .cornerRadius(4.dp)
                    .clickable(onClick),
            ) {
                Column(
                    modifier = GlanceModifier.padding(
                        start = (100 + 8).dp,
                        end = 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(family = "sans-serif"),
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(2.dp))
                    Text(
                        text = overview,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(family = "sans-serif"),
                            color = GlanceTheme.colors.onSurfaceVariant,
                        ),
                        maxLines = 4
                    )
                }
            }
            Image(
                provider = ImageProvider(bitmap = poster),
                contentScale = ContentScale.FillBounds,
                contentDescription = title,
                modifier = GlanceModifier
                    .size(height = 170.dp, width = 100.dp)
                    .padding(start = 8.dp, bottom = 8.dp)
                    .cornerRadius(8.0.dp)
            )
        }
    }
}