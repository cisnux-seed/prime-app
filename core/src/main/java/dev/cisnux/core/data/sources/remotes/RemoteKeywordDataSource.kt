package dev.cisnux.core.data.sources.remotes

import arrow.core.Either
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponseItem

interface RemoteKeywordDataSource {
    suspend fun getSuggestionKeywords(
        query: String,
        page: Int
    ): Either<Exception, List<KeywordResponseItem>>
}