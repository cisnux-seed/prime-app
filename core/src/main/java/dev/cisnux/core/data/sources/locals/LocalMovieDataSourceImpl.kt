package dev.cisnux.core.data.sources.locals

import androidx.paging.PagingSource
import androidx.room.withTransaction
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.utils.QueryUtil
import dev.cisnux.core.data.sources.locals.databases.PrimeDatabase
import dev.cisnux.core.data.sources.locals.databases.MovieEntity
import dev.cisnux.core.data.sources.locals.databases.MovieRemoteKeyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalMovieDataSourceImpl @Inject constructor(
    private val db: PrimeDatabase
) : LocalMovieDataSource {
    override suspend fun upsertLocalMovies(movies: List<MovieEntity>) =
        withContext(Dispatchers.IO) {
            db.movieDao().upsertMovieEntities(movies = movies)
        }

    override suspend fun upsertFavoriteMovie(movie: MovieEntity) = withContext(Dispatchers.IO) {
        db.movieDao().upsertMovieEntity(movie = movie)
    }

    override fun getLocalMovies(movieCategory: MovieCategory): PagingSource<Int, MovieEntity> =
        db.movieDao().getMovieEntities(query = QueryUtil.sortedQuery(movieCategory = movieCategory))

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> =
        db.movieDao()
            .getFavoriteMovieEntities()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override suspend fun onRefreshLocalMovies(
        movies: List<MovieEntity>,
        remoteKeys: List<MovieRemoteKeyEntity>,
        movieCategory: MovieCategory,
    ) {
        db.withTransaction {
            withContext(Dispatchers.IO) {
                db.remoteKeyDao().upsertRemoteKeys(remoteKeys = remoteKeys)
                db.movieDao().upsertMovieEntities(movies = movies)
            }
        }
    }

    override suspend fun getLocalMovieById(id: Int): MovieEntity? = withContext(Dispatchers.IO) {
        db.movieDao().getMovieEntityById(movieId = id)
    }

    override suspend fun getRemoteKeyById(id: Int): MovieRemoteKeyEntity? =
        withContext(Dispatchers.IO) {
            db.remoteKeyDao().getRemoteKeyById(id = id)
        }

    override suspend fun getCreationTime(): Long? = withContext(Dispatchers.IO) {
        db.remoteKeyDao().getCreationTime()
    }

    override fun getLocalMovieByIdAsFlow(movieId: Int): Flow<MovieEntity?> =
        db.movieDao()
            .getMovieEntityByIdAsFlow(movieId = movieId)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override fun getRandomWatchlist(): Flow<MovieEntity?> =
        db.movieDao().getRandomWatchlist()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

}