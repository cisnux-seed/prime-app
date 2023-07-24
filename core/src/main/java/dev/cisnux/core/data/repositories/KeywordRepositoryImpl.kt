package dev.cisnux.core.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.cisnux.core.data.paging.KeywordPagingSource
import dev.cisnux.core.data.sources.remotes.RemoteKeywordDataSource
import dev.cisnux.core.domain.repositories.KeywordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KeywordRepositoryImpl @Inject constructor(
    private val remoteKeywordDataSource: RemoteKeywordDataSource
) : KeywordRepository {
    override fun getKeywordSuggestions(query: String): Flow<PagingData<String>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                KeywordPagingSource(
                    query = query,
                    remoteKeywordDataSource = remoteKeywordDataSource
                )
            }
        ).flow
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
            .map {
                it.map { keyword -> keyword.name }
            }
}