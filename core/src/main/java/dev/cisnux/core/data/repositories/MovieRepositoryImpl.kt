package dev.cisnux.core.data.repositories

import android.content.Context
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.utils.UiState
import dev.cisnux.core.utils.asMovieEntity
import dev.cisnux.core.utils.duration
import dev.cisnux.core.utils.fromListMovieEntities
import dev.cisnux.core.utils.fromPagingMovieEntities
import dev.cisnux.core.utils.fromPagingMovieResponseItem
import dev.cisnux.core.data.paging.MovieRemoteMediator
import dev.cisnux.core.data.paging.RecommendMoviePagingSource
import dev.cisnux.core.data.paging.SearchMoviePagingSource
import dev.cisnux.core.data.sources.locals.LocalMovieDataSource
import dev.cisnux.core.data.sources.remotes.RemoteMovieDataSource
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.models.MovieWidget
import dev.cisnux.core.domain.repositories.MovieRepository
import dev.cisnux.core.utils.w500Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesByCategory(category: MovieCategory): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = MovieRemoteMediator(
            localMovieDataSource = localMovieDataSource,
            remoteMovieDataSource = remoteMovieDataSource,
            movieCategory = category
        ),
        pagingSourceFactory = {
            localMovieDataSource.getLocalMovies(movieCategory = category)
        }
    )
        .flow
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .map {
            it.fromPagingMovieEntities()
        }

    override fun getRecommendationMovies(movieId: Int): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            RecommendMoviePagingSource(
                movieId = movieId,
                remoteMovieDataSource = remoteMovieDataSource
            )
        }
    ).flow
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .map {
            it.fromPagingMovieResponseItem()
        }

    override fun getMoviesByQuery(query: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            SearchMoviePagingSource(
                query = query,
                remoteMovieDataSource = remoteMovieDataSource
            )
        }
    ).flow
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
        .map {
            it.fromPagingMovieResponseItem()
        }

    override suspend fun upsertFavoriteMovie(movie: MovieDetail) =
        localMovieDataSource.upsertFavoriteMovie(movie = movie.asMovieEntity())

    override fun getWatchlistMovies(): Flow<List<Movie>> =
        localMovieDataSource.getFavoriteMovies()
            .map {
                it.fromListMovieEntities()
            }

    override fun getRandomWatchlistForWidget(): Flow<MovieWidget?> = channelFlow {
        val loader = ImageLoader(context)
        localMovieDataSource.getRandomWatchlist()
            .collectLatest { movieEntity ->
                send(movieEntity?.let {
                    val request = ImageRequest.Builder(context)
                        .data(it.poster.w500Image)
                        .allowHardware(enable = true)
                        .build()
                    when (val result = loader.execute(request)) {
                        is SuccessResult -> {
                            result.drawable.toBitmapOrNull()?.let { bitmap ->
                                MovieWidget(
                                    id = it.id,
                                    title = it.title,
                                    poster = bitmap,
                                    overview = it.overview,
                                )
                            }
                        }

                        is ErrorResult -> {
                            null
                        }
                    }
                })
            }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)


    override fun getMovieById(movieId: Int): Flow<UiState<MovieDetail>> =
        channelFlow {
            send(UiState.Loading)
            val result = remoteMovieDataSource.getMovieById(movieId = movieId)
            result.fold({
                send(UiState.Error(it))
            }, {
                localMovieDataSource.getLocalMovieByIdAsFlow(movieId = it.id)
                    .distinctUntilChanged()
                    .flowOn(Dispatchers.IO)
                    .collectLatest { movieEntity ->
                        send(
                            UiState.Success(
                                MovieDetail(
                                    id = it.id,
                                    title = it.title,
                                    poster = it.posterPath,
                                    overview = it.overview,
                                    voteAverage = it.voteAverage,
                                    genres = it.genres.joinToString(", ") { genre ->
                                        genre.name
                                    },
                                    duration = it.runtime.duration(),
                                    movieCategory = movieEntity?.movieCategory,
                                    isFavorite = movieEntity?.isFavorite == true,
                                    nowPlayingPage = movieEntity?.nowPlayingPage,
                                    popularPage = movieEntity?.popularPage,
                                    topRatedPage = movieEntity?.topRatedPage
                                )
                            )
                        )
                    }
            })
        }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
}