package dev.cisnux.core.domain.usecases

import androidx.paging.PagingData
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.utils.UiState
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.models.MovieWidget
import dev.cisnux.core.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : MovieInteractor {
    override fun getMoviesByCategory(category: MovieCategory): Flow<PagingData<Movie>> =
        movieRepository.getMoviesByCategory(category = category)

    override fun getMovieById(movieId: Int): Flow<UiState<MovieDetail>> =
        movieRepository.getMovieById(movieId = movieId)

    override fun getRecommendationMovies(movieId: Int): Flow<PagingData<Movie>> =
        movieRepository.getRecommendationMovies(movieId = movieId)

    override fun getMoviesByQuery(query: String): Flow<PagingData<Movie>> =
        movieRepository.getMoviesByQuery(query = query)

    override suspend fun upsertFavoriteMovie(movie: MovieDetail) =
        movieRepository.upsertFavoriteMovie(movie = movie)

    override fun getWatchlistMovies(): Flow<List<Movie>> =
        movieRepository.getWatchlistMovies()

    override fun getRandomWatchlistForWidget(): Flow<MovieWidget?> =
        movieRepository.getRandomWatchlistForWidget()
}