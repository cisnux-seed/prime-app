package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.data.sources.remotes.responses.MovieDetailResponse
import dev.cisnux.core.dummy.dummyMovieDetailResponse
import dev.cisnux.core.dummy.dummyMovieResponses
import dev.cisnux.core.dummy.dummyNotFoundMovieJson
import dev.cisnux.core.dummy.dummyMovieDetailJson
import dev.cisnux.core.dummy.dummyEmptyMoviesJson
import dev.cisnux.core.dummy.dummyMoviesJson
import dev.cisnux.core.utils.Failure.Companion.MOVIES_HTTP_FAILURES
import dev.cisnux.core.utils.MovieCategory
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert

class RemoteMovieDataSourceTest : BaseRemoteTest() {

    @Test
    fun getMoviesByMovieCategory_WhenSuccess() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyMoviesJson),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            dummyMovieResponses,
            remoteDataSource.getMoviesByMovieCategory(
                category = MovieCategory.NOW_PLAYING_MOVIES,
                page = 1
            )
        )
    }

    @Test
    fun getMovieById_WhenSuccess() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyMovieDetailJson),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            dummyMovieDetailResponse,
            remoteDataSource.getMovieById(
                movieId = 667538
            )
        )
    }

    @Test
    fun getMoviesByQuery_WhenSuccess() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyMoviesJson),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            dummyMovieResponses,
            remoteDataSource.getMoviesByQuery(
                query = "transformers",
                page = 1,
            )
        )
    }

    @Test
    fun getMovieById_WhenNotFound() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyNotFoundMovieJson),
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            Either.Left(MOVIES_HTTP_FAILURES[HttpStatusCode.NotFound]),
            remoteDataSource.getMovieById(
                movieId = 667538
            )
        )
    }

    @Test
    fun getRecommendationMovies_WhenSuccess() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyMoviesJson),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            dummyMovieResponses,
            remoteDataSource.getRecommendationMovies(
                movieId = 1,
                page = 1,
            )
        )
    }

    @Test
    fun getMoviesByQuery_WhenSuccessButEmpty() = runTest {
        val remoteDataSource = RemoteMovieDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = dummyEmptyMoviesJson),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            Either.Right(listOf<MovieDetailResponse>()),
            remoteDataSource.getMoviesByQuery(
                query = "something",
                page = 1
            )
        )
    }
}