package dev.cisnux.prime.dummy

import androidx.paging.PagingData
import dev.cisnux.core.domain.usecases.KeywordInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface FakeKeywordUseCase {
    object Success : KeywordInteractor, FakeKeywordUseCase {
        override fun getKeywordSuggestions(query: String): Flow<PagingData<String>> = flow {
            emit(
                PagingData.from(
                    data = listOf(),
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.from(
                    data = dummyKeywords,
                    sourceLoadStates = dummySuccessLoadState,
                    mediatorLoadStates = dummySuccessLoadState
                )
            )
        }
    }
}
