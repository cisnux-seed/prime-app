package dev.cisnux.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.cisnux.core.data.sources.remotes.RemoteKeywordDataSource
import dev.cisnux.core.data.sources.remotes.responses.KeywordResponseItem

class KeywordPagingSource(
    private val query: String,
    private val remoteKeywordDataSource: RemoteKeywordDataSource
) : PagingSource<Int, KeywordResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, KeywordResponseItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, KeywordResponseItem> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        val result = remoteKeywordDataSource.getSuggestionKeywords(query = query, page = page)
        return result.fold({
            LoadResult.Error(it)
        }, {
            LoadResult.Page(
                data = it,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (it.isEmpty()) null else page + 1
            )
        })
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}