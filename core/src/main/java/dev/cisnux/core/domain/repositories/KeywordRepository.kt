package dev.cisnux.core.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface KeywordRepository {
    fun getKeywordSuggestions(query: String): Flow<PagingData<String>>
}