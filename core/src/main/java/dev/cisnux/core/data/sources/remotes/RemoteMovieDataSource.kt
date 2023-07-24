package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.data.sources.remotes.responses.MovieDetailResponse
import dev.cisnux.core.data.sources.remotes.responses.MovieResponseItem

interface RemoteMovieDataSource {
    suspend fun getMoviesByMovieCategory(category: MovieCategory, page: Int): Either<Exception, List<MovieResponseItem>>
    suspend fun getMovieById(movieId: Int): Either<Exception, MovieDetailResponse>
    suspend fun getRecommendationMovies(movieId: Int, page: Int): Either<Exception, List<MovieResponseItem>>
    suspend fun getMoviesByQuery(query: String, page: Int): Either<Exception, List<MovieResponseItem>>
}