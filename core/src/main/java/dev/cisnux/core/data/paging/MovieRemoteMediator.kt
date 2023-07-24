package dev.cisnux.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.data.sources.locals.LocalMovieDataSource
import dev.cisnux.core.data.sources.locals.databases.MovieEntity
import dev.cisnux.core.data.sources.locals.databases.MovieRemoteKeyEntity
import dev.cisnux.core.data.sources.remotes.RemoteMovieDataSource
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val movieCategory: MovieCategory
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                when (movieCategory) {
                    MovieCategory.NOW_PLAYING_MOVIES -> remoteKeys?.nowPlayingNextKey?.minus(1)
                    MovieCategory.POPULAR_MOVIES -> remoteKeys?.popularNextKey?.minus(1)
                    MovieCategory.TOP_RATED_MOVIES -> remoteKeys?.topRatedNextKey?.minus(1)
                } ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = when (movieCategory) {
                    MovieCategory.NOW_PLAYING_MOVIES -> remoteKeys?.nowPlayingPrevKey
                    MovieCategory.POPULAR_MOVIES -> remoteKeys?.popularPrevKey
                    MovieCategory.TOP_RATED_MOVIES -> remoteKeys?.topRatedPrevKey
                } ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = when (movieCategory) {
                    MovieCategory.NOW_PLAYING_MOVIES -> remoteKeys?.nowPlayingNextKey
                    MovieCategory.POPULAR_MOVIES -> remoteKeys?.popularNextKey
                    MovieCategory.TOP_RATED_MOVIES -> remoteKeys?.topRatedNextKey
                } ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        val result = remoteMovieDataSource
            .getMoviesByMovieCategory(category = movieCategory, page = page)
        return result.fold({
            MediatorResult.Error(throwable = it)
        }, {
            val endOfPaginationReached = it.isEmpty()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1
            val remoteKeys = it.map { movie ->
                localMovieDataSource.getRemoteKeyById(id = movie.id)
                    // If the remote key entity not exists, create a new one again.
                    // Otherwise, update the remote key entity.
                    ?.let { remoteKey ->
                        remoteKey.copy(
                            nowPlayingPrevKey = if (remoteKey.nowPlayingPrevKey == null
                                && movieCategory == MovieCategory.NOW_PLAYING_MOVIES
                            ) prevKey
                            else remoteKey.nowPlayingPrevKey,
                            nowPlayingNextKey = if (remoteKey.nowPlayingNextKey == null
                                && movieCategory == MovieCategory.NOW_PLAYING_MOVIES
                            ) nextKey
                            else remoteKey.nowPlayingNextKey,
                            popularPrevKey = if (remoteKey.popularPrevKey == null
                                && movieCategory == MovieCategory.POPULAR_MOVIES
                            ) prevKey
                            else remoteKey.popularPrevKey,
                            popularNextKey = if (remoteKey.popularNextKey == null
                                && movieCategory == MovieCategory.POPULAR_MOVIES
                            ) nextKey
                            else remoteKey.popularNextKey,
                            topRatedPrevKey = if (remoteKey.topRatedPrevKey == null
                                && movieCategory == MovieCategory.TOP_RATED_MOVIES
                            ) prevKey
                            else remoteKey.topRatedPrevKey,
                            topRatedNextKey = if (remoteKey.topRatedNextKey == null
                                && movieCategory == MovieCategory.TOP_RATED_MOVIES
                            ) nextKey
                            else remoteKey.topRatedNextKey,
                        )
                    } ?: MovieRemoteKeyEntity(
                    id = movie.id,
                    nowPlayingPrevKey = if (movieCategory == MovieCategory.NOW_PLAYING_MOVIES) prevKey
                    else null,
                    nowPlayingNextKey = if (movieCategory == MovieCategory.NOW_PLAYING_MOVIES) nextKey
                    else null,
                    popularPrevKey = if (movieCategory == MovieCategory.POPULAR_MOVIES) prevKey
                    else null,
                    popularNextKey = if (movieCategory == MovieCategory.POPULAR_MOVIES) nextKey
                    else null,
                    topRatedPrevKey = if (movieCategory == MovieCategory.TOP_RATED_MOVIES) prevKey
                    else null,
                    topRatedNextKey = if (movieCategory == MovieCategory.TOP_RATED_MOVIES) nextKey
                    else null
                )
            }

            localMovieDataSource.onRefreshLocalMovies(
                movies = it
                    .filter { movie -> movie.posterPath != null && movie.overview?.isNotBlank() == true }
                    .map { movieResponseItem ->
                        localMovieDataSource
                            .getLocalMovieById(id = movieResponseItem.id)
                            // If the movie entity not exists, create a new one again.
                            // Otherwise, update the film entity.
                            ?.let { movieEntity ->
                                movieEntity.copy(
                                    title = movieResponseItem.title,
                                    poster = movieResponseItem.posterPath!!,
                                    overview = movieResponseItem.overview!!,
                                    movieCategory = movieEntity.movieCategory?.let {
                                        "${movieEntity.movieCategory},${movieCategory.value}"
                                    } ?: movieCategory.value,
                                    nowPlayingPage = if (movieEntity.nowPlayingPage == null
                                        && movieCategory == MovieCategory.NOW_PLAYING_MOVIES
                                    ) page
                                    else movieEntity.nowPlayingPage,
                                    popularPage = if (movieEntity.popularPage == null
                                        && movieCategory == MovieCategory.POPULAR_MOVIES
                                    ) page
                                    else movieEntity.popularPage,
                                    topRatedPage = if (movieEntity.topRatedPage == null
                                        && movieCategory == MovieCategory.TOP_RATED_MOVIES
                                    ) page
                                    else movieEntity.topRatedPage,
                                )
                            } ?: MovieEntity(
                            id = movieResponseItem.id,
                            title = movieResponseItem.title,
                            poster = movieResponseItem.posterPath!!,
                            movieCategory = movieCategory.value,
                            overview = movieResponseItem.overview!!,
                            nowPlayingPage = if (movieCategory == MovieCategory.NOW_PLAYING_MOVIES) page else null,
                            popularPage = if (movieCategory == MovieCategory.POPULAR_MOVIES) page else null,
                            topRatedPage = if (movieCategory == MovieCategory.TOP_RATED_MOVIES) page else null
                        )
                    },
                remoteKeys = remoteKeys,
                movieCategory = movieCategory
            )
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        })
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>) =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localMovieDataSource.getRemoteKeyById(data.id)
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localMovieDataSource.getRemoteKeyById(data.id)
        }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localMovieDataSource.getRemoteKeyById(id)
            }
        }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)

        return if (System.currentTimeMillis() - (localMovieDataSource.getCreationTime()
                ?: 0) < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}