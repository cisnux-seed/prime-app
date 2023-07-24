package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponseItem
import dev.cisnux.core.dummy.fakeKeywordResponses
import dev.cisnux.core.dummy.jsonEmptyKeywordsString
import dev.cisnux.core.dummy.jsonKeywordsString
import dev.cisnux.core.dummy.jsonServerErrorString
import dev.cisnux.core.utils.Failure.Companion.KEYWORD_HTTP_FAILURES
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RemoteKeywordDataSourceTest : BaseRemoteTest() {

    @Test
    fun getSuggestionKeywords_WhenSuccess() = runTest {
        val remoteDataSource = RemoteKeywordDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = jsonKeywordsString),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            fakeKeywordResponses,
            remoteDataSource.getSuggestionKeywords(
                query = "test",
                page = 1
            )
        )
    }

    @Test
    fun getSuggestionKeywords_WhenSuccessButEmpty() = runTest {
        val remoteDataSource = RemoteKeywordDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = jsonEmptyKeywordsString),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            Either.Right(listOf<KeywordResponseItem>()),
            remoteDataSource.getSuggestionKeywords(
                query = "test",
                page = 1
            )
        )
    }

    @Test
    fun getSuggestionKeywords_WhenInternalServerError() = runTest {
        val remoteDataSource = RemoteKeywordDataSourceImpl(client = mockHandler {
            respond(
                content = ByteReadChannel(text = jsonServerErrorString),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })
        Assert.assertEquals(
            Either.Left(KEYWORD_HTTP_FAILURES[HttpStatusCode.InternalServerError]),
            remoteDataSource.getSuggestionKeywords(
                query = "test",
                page = 1
            )
        )
    }
}