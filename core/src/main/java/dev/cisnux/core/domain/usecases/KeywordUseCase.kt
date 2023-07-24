package dev.cisnux.core.domain.usecases

import androidx.paging.PagingData
import dev.cisnux.core.domain.repositories.KeywordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KeywordUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository
) : KeywordInteractor {
    override fun getKeywordSuggestions(query: String): Flow<PagingData<String>> =
        keywordRepository.getKeywordSuggestions(query = query)
}