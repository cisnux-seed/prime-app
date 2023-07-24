package dev.cisnux.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.cisnux.core.data.sources.remotes.RemoteMovieDataSource
import dev.cisnux.core.data.sources.remotes.responses.MovieResponseItem

class SearchMoviePagingSource(
    private val query: String,
    private val remoteMovieDataSource: RemoteMovieDataSource,
) : PagingSource<Int, MovieResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, MovieResponseItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponseItem> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        val result = remoteMovieDataSource.getMoviesByQuery(
            query = query,
            page = page
        )
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