package dev.cisnux.prime.presentation.detail

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.shimmer
import dev.cisnux.prime.presentation.ui.components.MovieCardPlaceholder
import dev.cisnux.prime.presentation.ui.components.RatingBar
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import dev.cisnux.prime.presentation.ui.components.MovieCard
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.utils.UiState
import dev.cisnux.core.utils.duration
import dev.cisnux.core.utils.originalImage
import dev.cisnux.core.utils.toRound
import dev.cisnux.prime.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navigateUp: () -> Unit,
    navigateToMovieDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState()
    )
    val context = LocalContext.current
    val recommendationMovies = movieDetailViewModel
        .recommendationMovies
        .collectAsLazyPagingItems()
    val movieDetailState by movieDetailViewModel.movieDetailState.collectAsStateWithLifecycle()

    when {
        movieDetailState is UiState.Success -> {
            LaunchedEffect(Unit) {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }

        movieDetailState is UiState.Loading -> {
            LaunchedEffect(Unit) {
                scaffoldState.bottomSheetState.hide()
            }
        }

        movieDetailState is UiState.Error -> {
            (movieDetailState as UiState.Error).error?.let { exception ->
                LaunchedEffect(snackbarHostState) {
                    scaffoldState.bottomSheetState.hide()
                    exception.message?.let {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = it,
                            actionLabel = context.getString(R.string.retry),
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            movieDetailViewModel.getMovieDetailById()
                            recommendationMovies.retry()
                        } else navigateUp()
                    }
                }
            }
        }

        recommendationMovies.loadState.refresh is LoadState.Error -> {
            val failure = (recommendationMovies.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                scaffoldState.bottomSheetState.hide()
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        movieDetailViewModel.getMovieDetailById()
                        recommendationMovies.retry()
                    } else navigateUp()
                }
            }
        }
    }


    MovieDetailContent(
        sheetContent = {
            if (movieDetailState is UiState.Success<MovieDetail>) {
                val movieDetail = (movieDetailState as UiState.Success<MovieDetail>).data!!
                SheetContent(
                    title = movieDetail.title,
                    genres = movieDetail.genres,
                    duration = movieDetail.overview,
                    voteAverage = movieDetail.voteAverage,
                    overview = movieDetail.overview,
                    isWatchlist = movieDetail.isFavorite,
                    recommendationMovies = recommendationMovies,
                    recommendMoviesRefreshLoading = (recommendationMovies.loadState.refresh is LoadState.Loading
                            || recommendationMovies.loadState.refresh is LoadState.Error) and (recommendationMovies.itemCount == 0),
                    recommendMoviesAppendLoading = recommendationMovies.loadState.append is LoadState.Loading,
                    onWatchlistChanged = { isWatchlist ->
                        movieDetailViewModel.onWatchlistChanged(
                            movieDetail = movieDetail.copy(
                                isFavorite = isWatchlist
                            )
                        )
                        coroutineScope.launch {
                            val snackbarResult = snackbarHostState.showSnackbar(
                                message = if (isWatchlist) context.getString(R.string.added_to_watchlist)
                                else context.getString(R.string.removed_from_watchlist),
                                actionLabel = context.getString(R.string.undo),
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            if (snackbarResult == SnackbarResult.ActionPerformed)
                                movieDetailViewModel.onWatchlistChanged(
                                    movieDetail = movieDetail.copy(
                                        isFavorite = !isWatchlist
                                    )
                                )
                        }
                    },
                    navigateToMovieDetail = navigateToMovieDetail
                )
            }
        },
        snackbarHostState = snackbarHostState,
        body = { innerPadding ->
            Box(
                modifier = modifier
            ) {
                if (movieDetailState is UiState.Success<MovieDetail>) {
                    val movieDetail = (movieDetailState as UiState.Success<MovieDetail>).data!!
                    MovieDetailBody(
                        poster = movieDetail.poster.originalImage,
                        contentDescription = movieDetail.title,
                        modifier = Modifier
                            .padding(paddingValues = innerPadding)
                            .align(Alignment.Center)
                    )
                } else
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                IconButton(
                    onClick = navigateUp,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                        .drawBehind {
                            drawCircle(color = Color.Black, alpha = 0.5f)
                        }
                        .align(Alignment.TopStart),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.back_to_previous_page)
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MovieDetailContentLightModePreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    PrimeTheme {
        MovieDetailContent(
            sheetContent = {
                SheetContent(
                    title = "Spider-Man: Across the Spider-Verse",
                    genres = "Action, Adventure, Animation, Science Fiction",
                    duration = 140.duration(),
                    voteAverage = 7.5f,
                    overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters the Spider Society, a team of Spider-People charged with protecting the Multiverse’s very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must set out on his own to save those he loves most.",
                    isWatchlist = false,
                    recommendationMovies = fakeData,
                    recommendMoviesRefreshLoading = true,
                    recommendMoviesAppendLoading = false,
                    onWatchlistChanged = {},
                    navigateToMovieDetail = {}
                )
            },
            snackbarHostState = snackbarHostState,
            body = { innerPadding ->
                MovieDetailBody(
                    contentDescription = "Spider-Man: Across the Spider-Verse",
                    poster = "8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg".originalImage,
                    modifier = Modifier.padding(paddingValues = innerPadding)
                )
            },
            scaffoldState = scaffoldState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun MovieDetailContentNightModePreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    PrimeTheme {
        MovieDetailContent(
            sheetContent = {
                SheetContent(
                    title = "Spider-Man: Across the Spider-Verse",
                    genres = "Action, Adventure, Animation, Science Fiction",
                    duration = 140.duration(),
                    voteAverage = 7.5f,
                    overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters the Spider Society, a team of Spider-People charged with protecting the Multiverse’s very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must set out on his own to save those he loves most.",
                    isWatchlist = true,
                    recommendationMovies = fakeData,
                    recommendMoviesRefreshLoading = true,
                    recommendMoviesAppendLoading = false,
                    onWatchlistChanged = {},
                    navigateToMovieDetail = {}
                )
            },
            snackbarHostState = snackbarHostState,
            body = { innerPadding ->
                MovieDetailBody(
                    contentDescription = "Spider-Man: Across the Spider-Verse",
                    poster = "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg".originalImage,
                    modifier = Modifier.padding(paddingValues = innerPadding)
                )
            },
            scaffoldState = scaffoldState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailContent(
    sheetContent: @Composable ColumnScope.() -> Unit,
    snackbarHostState: SnackbarHostState,
    scaffoldState: BottomSheetScaffoldState,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 160.dp,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        sheetContent = sheetContent,
        modifier = modifier
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
private fun MovieDetailBody(
    poster: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = poster,
        contentScale = ContentScale.FillBounds,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxSize()
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun SheetContentPreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()

    PrimeTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SheetContent(
                title = "Spider-Man: Across the Spider-Verse",
                genres = "Action, Adventure, Animation, Science Fiction",
                duration = 140.duration(),
                voteAverage = 7.5f,
                overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters the Spider Society, a team of Spider-People charged with protecting the Multiverse’s very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must set out on his own to save those he loves most.",
                isWatchlist = false,
                recommendationMovies = fakeData,
                recommendMoviesRefreshLoading = true,
                recommendMoviesAppendLoading = false,
                onWatchlistChanged = {},
                navigateToMovieDetail = {}
            )
        }
    }
}

@Composable
private fun SheetContent(
    title: String,
    genres: String,
    duration: String,
    voteAverage: Float,
    overview: String,
    isWatchlist: Boolean,
    recommendationMovies: LazyPagingItems<Movie>,
    recommendMoviesRefreshLoading: Boolean,
    recommendMoviesAppendLoading: Boolean,
    onWatchlistChanged: (isWatchlist: Boolean) -> Unit,
    navigateToMovieDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(
            paddingValues = PaddingValues(horizontal = 16.dp)
        )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Button(onClick = {
            onWatchlistChanged(!isWatchlist)
        }) {
            Icon(
                imageVector = if (!isWatchlist) Icons.Filled.Add
                else Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = if (!isWatchlist) stringResource(R.string.add_to_watchlist) else stringResource(
                    R.string.remove_from_watchlist
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = genres,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W700,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = duration,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W700,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingBar(rating = voteAverage / 2f)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = voteAverage.toRound().toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = overview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (recommendMoviesRefreshLoading || recommendationMovies.itemCount > 0) {
            Text(
                text = stringResource(R.string.recommendations),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                item {
                    AnimatedVisibility(visible = recommendMoviesRefreshLoading) {
                        Row {
                            repeat(4) { index ->
                                MovieCardPlaceholder(
                                    modifier = Modifier
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .size(height = 220.dp, width = 120.dp)
                                        .shimmer()
                                )
                                if (index != 3)
                                    Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
                items(
                    count = recommendationMovies.itemCount,
                    contentType = recommendationMovies.itemContentType { it }
                ) { index ->
                    val movie = recommendationMovies[index]
                    movie?.let {
                        MovieCard(
                            title = movie.title,
                            poster = movie.poster.originalImage,
                            onClick = {
                                navigateToMovieDetail(movie.id)
                            })
                    }
                }
                if (recommendMoviesAppendLoading) {
                    item {
                        MovieCardPlaceholder(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .size(height = 220.dp, width = 120.dp)
                                .shimmer()
                        )
                    }
                }
            },
            contentPadding = PaddingValues(bottom = 16.dp)
        )
    }
}

