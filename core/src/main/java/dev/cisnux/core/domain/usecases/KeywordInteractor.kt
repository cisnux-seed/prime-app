package dev.cisnux.core.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface KeywordInteractor {
    fun getKeywordSuggestions(query: String): Flow<PagingData<String>>
}