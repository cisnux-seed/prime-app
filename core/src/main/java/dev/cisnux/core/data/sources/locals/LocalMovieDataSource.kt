package dev.cisnux.core.data.sources.locals

import androidx.paging.PagingSource
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.data.sources.locals.databases.MovieEntity
import dev.cisnux.core.data.sources.locals.databases.MovieRemoteKeyEntity
import kotlinx.coroutines.flow.Flow

interface LocalMovieDataSource {
    suspend fun upsertLocalMovies(movies: List<MovieEntity>)
    suspend fun upsertFavoriteMovie(movie: MovieEntity)
    fun getLocalMovies(movieCategory: MovieCategory): PagingSource<Int, MovieEntity>
    fun getFavoriteMovies(): Flow<List<MovieEntity>>
    suspend fun onRefreshLocalMovies(
        movies: List<MovieEntity>,
        remoteKeys: List<MovieRemoteKeyEntity>,
        movieCategory: MovieCategory,
    )
    suspend fun getLocalMovieById(id: Int): MovieEntity?
    suspend fun getRemoteKeyById(id: Int): MovieRemoteKeyEntity?
    suspend fun getCreationTime(): Long?
    fun getLocalMovieByIdAsFlow(movieId: Int): Flow<MovieEntity?>
    fun getRandomWatchlist(): Flow<MovieEntity?>
}