package dev.cisnux.core.domain.usecases

import androidx.paging.PagingData
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.utils.UiState
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.models.MovieWidget
import kotlinx.coroutines.flow.Flow

interface MovieInteractor {
    fun getMoviesByCategory(category: MovieCategory): Flow<PagingData<Movie>>
    fun getMovieById(movieId: Int): Flow<UiState<MovieDetail>>
    fun getRecommendationMovies(movieId: Int): Flow<PagingData<Movie>>
    fun getMoviesByQuery(query: String): Flow<PagingData<Movie>>
    suspend fun upsertFavoriteMovie(movie: MovieDetail)
    fun getWatchlistMovies(): Flow<List<Movie>>
    fun getRandomWatchlistForWidget(): Flow<MovieWidget?>
}