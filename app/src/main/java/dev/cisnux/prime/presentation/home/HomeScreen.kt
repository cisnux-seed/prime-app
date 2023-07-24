package dev.cisnux.prime.presentation.home

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valentinilk.shimmer.shimmer
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.utils.originalImage
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.ui.components.BottomBar
import dev.cisnux.prime.presentation.ui.components.EmptyContents
import dev.cisnux.prime.presentation.ui.components.MovieCard
import dev.cisnux.prime.presentation.ui.components.MovieCardPlaceholder
import dev.cisnux.prime.presentation.ui.components.MovieCardTile
import dev.cisnux.prime.presentation.ui.components.MovieCardTileShimmer
import dev.cisnux.prime.presentation.ui.components.PrimeSearchBar
import dev.cisnux.prime.presentation.ui.components.rememberSearchBarState
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import dev.cisnux.prime.presentation.utils.AppDestination
import dev.cisnux.prime.presentation.utils.NavigationItems
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    navigateToMovieDetail: (id: Int) -> Unit,
    navigateForBottomNav: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true

    val nowPlayingMovies = homeViewModel.nowPlayingMovies.collectAsLazyPagingItems()
    val popularMovies = homeViewModel.popularMovies.collectAsLazyPagingItems()
    val topRatedMovies = homeViewModel.topRatedMovies.collectAsLazyPagingItems()
    val searchedMovies = homeViewModel.searchedMovies.collectAsLazyPagingItems()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val keywordSuggestions = homeViewModel.keywordSuggestions.collectAsLazyPagingItems()
    val scrollState = rememberScrollState()
    var searchBarState by rememberSearchBarState(
        initialQuery = "",
        initialActive = false,
        initialSearch = false
    )
    val context = LocalContext.current

    when {
        nowPlayingMovies.loadState.refresh is LoadState.Error -> {
            val failure = (nowPlayingMovies.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed)
                        nowPlayingMovies.retry()
                }
            }
        }

        popularMovies.loadState.refresh is LoadState.Error -> {
            val failure = (popularMovies.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed)
                        popularMovies.retry()
                }
            }
        }

        topRatedMovies.loadState.refresh is LoadState.Error -> {
            val failure = (topRatedMovies.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed)
                        topRatedMovies.retry()
                }
            }
        }

        searchedMovies.loadState.refresh is LoadState.Error -> {
            val failure = (searchedMovies.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed)
                        searchedMovies.retry()
                }
            }
        }

        keywordSuggestions.loadState.refresh is LoadState.Error -> {
            val failure = (keywordSuggestions.loadState.refresh as LoadState.Error).error
            LaunchedEffect(snackbarHostState) {
                failure.message?.let {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = it,
                        actionLabel = context.getString(R.string.retry),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed)
                        keywordSuggestions.retry()
                }
            }
        }
    }

    HomeContent(
        snackbarHostState = snackbarHostState,
        body = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(visible = !searchBarState.isSearch) {
                    HomeBody(
                        nowPlayingMovies = nowPlayingMovies,
                        popularMovies = popularMovies,
                        topRatedMovies = topRatedMovies,
                        nowPlayingMoviesRefreshLoading = (nowPlayingMovies.loadState.refresh is LoadState.Loading || nowPlayingMovies.loadState.refresh is LoadState.Error) and (nowPlayingMovies.itemCount == 0),
                        nowPlayingMoviesAppendLoading = nowPlayingMovies.loadState.append is LoadState.Loading,
                        popularMoviesRefreshLoading = (popularMovies.loadState.refresh is LoadState.Loading || popularMovies.loadState.refresh is LoadState.Error) and (popularMovies.itemCount == 0),
                        popularMoviesAppendLoading = popularMovies.loadState.append is LoadState.Loading,
                        topRatedMoviesRefreshLoading = (topRatedMovies.loadState.refresh is LoadState.Loading || topRatedMovies.loadState.refresh is LoadState.Error) and (topRatedMovies.itemCount == 0),
                        topRatedMoviesAppendLoading = topRatedMovies.loadState.append is LoadState.Loading,
                        scrollState = scrollState,
                        navigateToMovieDetail = navigateToMovieDetail,
                        context = context,
                        modifier = modifier.padding(paddingValues = innerPadding)
                    )
                }
                AnimatedVisibility(visible = searchBarState.isSearch) {
                    if (searchedMovies.itemCount == 0 && searchedMovies.loadState.refresh is LoadState.NotLoading)
                        EmptyContents(
                            label = stringResource(R.string.movies_not_found),
                            painter = painterResource(id = R.drawable.empty_watchlist),
                            contentDescription = "Movies not found"
                        )
                    else
                        SearchBody(
                            searchedMovies = searchedMovies,
                            searchedMoviesRefreshLoading = searchedMovies.loadState.refresh is LoadState.Loading || searchedMovies.loadState.refresh is LoadState.Error,
                            searchedMoviesAppendLoading = searchedMovies.loadState.append is LoadState.Loading,
                            navigateToMovieDetail = navigateToMovieDetail,
                            modifier = modifier.padding(paddingValues = innerPadding)
                        )
                }
                PrimeSearchBar(
                    query = searchBarState.query,
                    onQueryChange = {
                        searchBarState = searchBarState.copy(query = it)
                        homeViewModel.updateQuery(newQuery = it)
                    },
                    active = searchBarState.active,
                    onActiveChange = { isActive ->
                        searchBarState = searchBarState.copy(active = isActive)
                    },
                    keywordSuggestions = keywordSuggestions,
                    navigateUp = {
                        searchBarState =
                            searchBarState.copy(isSearch = false, active = false, query = "")
                        homeViewModel.doSearch(newSearch = false)
                    },
                    isSearch = searchBarState.isSearch,
                    onSearchChange = { query, isSearch ->
                        searchBarState = searchBarState.copy(
                            query = query,
                            isSearch = isSearch,
                            active = !isSearch
                        )
                        homeViewModel.doSearch(newSearch = isSearch)
                    },
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        onSelectedDestination = navigateForBottomNav,
        shouldBottomBarOpen = !searchBarState.active
    )
}


@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
private fun HomeContentPreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()
    var searchBarState by rememberSearchBarState(
        initialQuery = "",
        initialActive = false,
        initialSearch = false
    )
    val fakeKeywords = MutableStateFlow(PagingData.empty<String>()).collectAsLazyPagingItems()
    val context = LocalContext.current

    PrimeTheme {
        HomeContent(
            snackbarHostState = SnackbarHostState(),
            body = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HomeBody(
                        nowPlayingMovies = fakeData,
                        popularMovies = fakeData,
                        topRatedMovies = fakeData,
                        nowPlayingMoviesRefreshLoading = true,
                        nowPlayingMoviesAppendLoading = false,
                        popularMoviesRefreshLoading = true,
                        popularMoviesAppendLoading = false,
                        topRatedMoviesRefreshLoading = true,
                        topRatedMoviesAppendLoading = false,
                        scrollState = rememberScrollState(),
                        navigateToMovieDetail = {},
                        context = context,
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                    PrimeSearchBar(
                        query = searchBarState.query,
                        onQueryChange = { searchBarState = searchBarState.copy(query = it) },
                        active = searchBarState.active,
                        onActiveChange = { searchBarState = searchBarState.copy(active = it) },
                        keywordSuggestions = fakeKeywords,
                        navigateUp = {},
                        isSearch = searchBarState.isSearch,
                        onSearchChange = { _, _ -> },
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            },
            onSelectedDestination = { _, _ -> },
            shouldBottomBarOpen = !searchBarState.active
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun HomeContent(
    snackbarHostState: SnackbarHostState,
    onSelectedDestination: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    body: @Composable (innerPadding: PaddingValues) -> Unit,

    shouldBottomBarOpen: Boolean,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            AnimatedVisibility(visible = shouldBottomBarOpen) {
                BottomBar(
                    currentRoute = AppDestination.HomeRoute,
                    onSelectedDestination = onSelectedDestination,
                    navigationItems = NavigationItems
                )
            }
        },
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        }
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyWhenRefreshLoadingPreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()

    PrimeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeBody(
                nowPlayingMovies = fakeData,
                popularMovies = fakeData,
                topRatedMovies = fakeData,
                nowPlayingMoviesRefreshLoading = true,
                nowPlayingMoviesAppendLoading = false,
                popularMoviesRefreshLoading = true,
                popularMoviesAppendLoading = false,
                topRatedMoviesRefreshLoading = true,
                topRatedMoviesAppendLoading = false,
                scrollState = rememberScrollState(),
                navigateToMovieDetail = {},
                context = LocalContext.current
            )
        }
    }
}

@Composable
private fun HomeBody(
    nowPlayingMovies: LazyPagingItems<Movie>,
    popularMovies: LazyPagingItems<Movie>,
    topRatedMovies: LazyPagingItems<Movie>,
    nowPlayingMoviesRefreshLoading: Boolean,
    nowPlayingMoviesAppendLoading: Boolean,
    popularMoviesRefreshLoading: Boolean,
    popularMoviesAppendLoading: Boolean,
    topRatedMoviesRefreshLoading: Boolean,
    topRatedMoviesAppendLoading: Boolean,
    scrollState: ScrollState,
    navigateToMovieDetail: (id: Int) -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .fillMaxWidth()
            .padding(paddingValues = PaddingValues(top = (58 + 16).dp, bottom = 8.dp)),
    ) {
        Text(
            text = stringResource(R.string.now_playing),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            content = {
                item {
                    AnimatedVisibility(visible = nowPlayingMoviesRefreshLoading) {
                        Row {
                            repeat(4) {
                                MovieCardPlaceholder(
                                    modifier = Modifier
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .size(height = 220.dp, width = 120.dp)
                                        .shimmer()
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                items(count = nowPlayingMovies.itemCount,
                    key = nowPlayingMovies.itemKey { it.id },
                    contentType = nowPlayingMovies.itemContentType { it }) {
                    val movie = nowPlayingMovies[it]
                    movie?.let {
                        MovieCard(
                            title = movie.title,
                            poster = movie.poster.originalImage,
                            onClick = {
                            navigateToMovieDetail(movie.id)
                        })
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                if (nowPlayingMoviesAppendLoading) {
                    item {
                        MovieCardPlaceholder(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .size(height = 220.dp, width = 120.dp)
                                .shimmer()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            },
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.now_playing_content_desc)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.popular),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            content = {
                item {
                    AnimatedVisibility(visible = popularMoviesRefreshLoading) {
                        Row {
                            repeat(4) {
                                MovieCardPlaceholder(
                                    modifier = Modifier
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .size(height = 220.dp, width = 120.dp)
                                        .shimmer()
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
                items(count = popularMovies.itemCount,
                    key = popularMovies.itemKey { it.id },
                    contentType = popularMovies.itemContentType { it }) {
                    val movie = popularMovies[it]
                    movie?.let {
                        MovieCard(title = movie.title,
                            poster = movie.poster.originalImage,
                            onClick = { navigateToMovieDetail(movie.id) })
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                if (popularMoviesAppendLoading) {
                    item {
                        MovieCardPlaceholder(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .size(height = 220.dp, width = 120.dp)
                                .shimmer()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            },
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.popular_movies_content_desc)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.top_rated),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            content = {
                item {
                    AnimatedVisibility(visible = topRatedMoviesRefreshLoading) {
                        Row {
                            repeat(4) {
                                MovieCardPlaceholder(
                                    modifier = Modifier
                                        .clip(shape = MaterialTheme.shapes.small)
                                        .size(height = 220.dp, width = 120.dp)
                                        .shimmer()
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
                items(count = topRatedMovies.itemCount,
                    key = topRatedMovies.itemKey { it.id },
                    contentType = topRatedMovies.itemContentType { it }) {
                    val movie = topRatedMovies[it]
                    movie?.let {
                        MovieCard(
                            title = movie.title,
                            poster = movie.poster.originalImage,
                            onClick = {
                                navigateToMovieDetail(movie.id)
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                if (topRatedMoviesAppendLoading) {
                    item {
                        MovieCardPlaceholder(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .size(height = 220.dp, width = 120.dp)
                                .shimmer()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            },
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.top_rated_movies_content_desc)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBodyPreview() {
    val fakeData = MutableStateFlow(PagingData.empty<Movie>()).collectAsLazyPagingItems()

    PrimeTheme {
        SearchBody(
            searchedMovies = fakeData,
            searchedMoviesRefreshLoading = true,
            searchedMoviesAppendLoading = false,
            navigateToMovieDetail = {}
        )
    }
}

@Composable
private fun SearchBody(
    searchedMovies: LazyPagingItems<Movie>,
    searchedMoviesRefreshLoading: Boolean,
    searchedMoviesAppendLoading: Boolean,
    navigateToMovieDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            item {
                AnimatedVisibility(visible = searchedMoviesRefreshLoading) {
                    Column {
                        repeat(5) {
                            MovieCardTileShimmer()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            items(
                count = searchedMovies.itemCount,
                contentType = searchedMovies.itemContentType { it }) {
                val movie = searchedMovies[it]
                movie?.let {
                    MovieCardTile(
                        title = movie.title,
                        overview = movie.overview,
                        poster = movie.poster.originalImage,
                        onClick = {
                            navigateToMovieDetail(movie.id)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if (searchedMoviesAppendLoading) {
                item {
                    MovieCardTileShimmer()
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        },
        contentPadding = PaddingValues(
            top = (58 + 16).dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        ),
        modifier = modifier,
    )
}