package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.utils.BASE_URL_MOVIE
import dev.cisnux.core.utils.Failure
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponse
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponseItem
import dev.cisnux.core.utils.Failure.Companion.KEYWORD_HTTP_FAILURES
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RemoteKeywordDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : RemoteKeywordDataSource {
    override suspend fun getSuggestionKeywords(
        query: String,
        page: Int
    ): Either<Exception, List<KeywordResponseItem>> = withContext(Dispatchers.IO) {
        try {
            val response = client.get(
                urlString = "$BASE_URL_MOVIE/search/keyword?query=$query&page=$page"
            )
            val failure = KEYWORD_HTTP_FAILURES[response.status]
            return@withContext if (failure != null) {
                Either.Left(failure)
            } else {
                val data: KeywordResponse = response.body()
                Either.Right(data.results)
            }
        } catch (e: UnresolvedAddressException) {
            Either.Left(Failure.ConnectionFailure())
        }
    }
}