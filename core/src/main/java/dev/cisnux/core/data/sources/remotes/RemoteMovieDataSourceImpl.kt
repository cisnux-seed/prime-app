package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.utils.BASE_URL_MOVIE
import dev.cisnux.core.utils.Failure
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.data.sources.remotes.responses.MovieDetailResponse
import dev.cisnux.core.data.sources.remotes.responses.MovieResponseItem
import dev.cisnux.core.data.sources.remotes.responses.MovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteMovieDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : RemoteMovieDataSource {
    override suspend fun getMoviesByMovieCategory(
        category: MovieCategory,
        page: Int
    ): Either<Exception, List<MovieResponseItem>> =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(
                    urlString = "$BASE_URL_MOVIE/movie/${category.value}?page=$page"
                )
                val failure = Failure.MOVIES_HTTP_FAILURES[response.status]
                return@withContext if (failure != null) {
                    Either.Left(failure)
                } else {
                    val data: MovieResponse = response.body()
                    Either.Right(data.results)
                }
            } catch (e: UnresolvedAddressException) {
                Either.Left(Failure.ConnectionFailure())
            }
        }

    override suspend fun getMovieById(movieId: Int): Either<Exception, MovieDetailResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(
                    urlString = "$BASE_URL_MOVIE/movie/$movieId"
                )
                val failure = Failure.MOVIES_HTTP_FAILURES[response.status]
                return@withContext if (failure != null) {
                    Either.Left(failure)
                } else {
                    val data: MovieDetailResponse = response.body()
                    Either.Right(data)
                }
            } catch (e: UnresolvedAddressException) {
                Either.Left(Failure.ConnectionFailure())
            }
        }

    override suspend fun getRecommendationMovies(
        movieId: Int,
        page: Int
    ): Either<Exception, List<MovieResponseItem>> =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(
                    urlString = "$BASE_URL_MOVIE/movie/$movieId/recommendations?page=$page"
                )
                val failure = Failure.MOVIES_HTTP_FAILURES[response.status]
                return@withContext if (failure != null) {
                    Either.Left(failure)
                } else {
                    val data: MovieResponse = response.body()
                    Either.Right(data.results)
                }
            } catch (e: UnresolvedAddressException) {
                Either.Left(Failure.ConnectionFailure())
            } catch (e: Exception) {
                Either.Left(e)
            }
        }

    override suspend fun getMoviesByQuery(
        query: String,
        page: Int
    ): Either<Exception, List<MovieResponseItem>> =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(
                    urlString = "$BASE_URL_MOVIE/search/movie?page=$page&query=$query"
                )
                val failure = Failure.MOVIES_HTTP_FAILURES[response.status]
                return@withContext if (failure != null) {
                    Either.Left(failure)
                } else {
                    val data: MovieResponse = response.body()
                    Either.Right(data.results)
                }
            } catch (e: UnresolvedAddressException) {
                Either.Left(Failure.ConnectionFailure())
            }
        }
}