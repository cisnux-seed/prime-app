package dev.cisnux.core.data.sources.locals.databases

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import dev.cisnux.core.utils.MOVIE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieEntities(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovieEntity(movie: MovieEntity)

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getMovieEntities(query: SupportSQLiteQuery): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE favorite_movie = 1")
    fun getFavoriteMovieEntities(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE id = :movieId")
    suspend fun getMovieEntityById(movieId: Int): MovieEntity?

    @Query("SELECT * FROM $MOVIE_TABLE WHERE id = :movieId")
    fun getMovieEntityByIdAsFlow(movieId: Int): Flow<MovieEntity?>

    @Query("SELECT * FROM $MOVIE_TABLE WHERE favorite_movie = 1 ORDER BY RANDOM() LIMIT 1")
    fun getRandomWatchlist(): Flow<MovieEntity?>
}
