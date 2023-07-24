package dev.cisnux.favorite.presentation.watchlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.cisnux.prime.presentation.ui.components.BottomBar
import dev.cisnux.prime.presentation.ui.components.EmptyContents
import dev.cisnux.prime.presentation.ui.components.MovieCardTile
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import dev.cisnux.prime.presentation.utils.AppDestination
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.utils.ORIGINAL_IMAGE_URL
import dev.cisnux.core.utils.w500Image
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.utils.NavigationItems

@Composable
fun WatchlistMovieScreen(
    navigateToMovieDetail: (id: Int) -> Unit,
    navigateForBottomNav: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    watchlistMovieViewModel: WatchlistMovieViewModel,
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true
    val context = LocalContext.current

    val favoriteMovies by watchlistMovieViewModel.watchlistMovies.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    FavoriteMovieContent(
        body = { innerPadding ->
            if (favoriteMovies.isEmpty())
                EmptyContents(
                    label = stringResource(R.string.no_watchlist),
                    painter = painterResource(id = R.drawable.empty_watchlist),
                    contentDescription = "No watchlist",
                )
            else
                FavoriteMovieBody(
                    favoriteMovies = favoriteMovies,
                    navigateToMovieDetail = navigateToMovieDetail,
                    modifier = modifier
                        .semantics {
                            contentDescription =
                                context.getString(R.string.watchlist_movies_content_desc)
                        }
                        .padding(paddingValues = innerPadding)
                )
        },
        modifier = modifier,
        onSelectedDestination = navigateForBottomNav
    )
}

@Preview(showBackground = true)
@Composable
private fun FavoriteMovieContentPreview() {
    val fakeData = List(10) {
        Movie(
            id = it,
            title = "Spider-Man: Across the Spider-Verse",
            overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters the Spider Society, a team of Spider-People charged with protecting the Multiverse’s very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must set out on his own to save those he loves most.",
            poster = "$ORIGINAL_IMAGE_URL/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
        )
    }

    PrimeTheme {
        FavoriteMovieContent(
            body = { innerPadding ->
                FavoriteMovieBody(
                    favoriteMovies = fakeData,
                    navigateToMovieDetail = {},
                    modifier = Modifier.padding(paddingValues = innerPadding),
                )
            },
            onSelectedDestination = { _, _ -> }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteMovieContent(
    onSelectedDestination: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.watchlist),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = AppDestination.WatchlistRoute,
                onSelectedDestination = onSelectedDestination,
                navigationItems = NavigationItems
            )
        }
    )
    { innerPadding ->
        body(innerPadding)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteMovieBody(
    favoriteMovies: List<Movie>,
    navigateToMovieDetail: (id: Int) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        items(
            items = favoriteMovies,
            key = {
                it.id
            },
            contentType = { it }
        ) { movie ->
            MovieCardTile(
                title = movie.title,
                overview = movie.overview,
                poster = movie.poster.w500Image,
                onClick = {
                    navigateToMovieDetail(movie.id)
                },
                modifier = Modifier.animateItemPlacement()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}