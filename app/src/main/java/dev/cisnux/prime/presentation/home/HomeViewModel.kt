package dev.cisnux.prime.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.core.domain.usecases.KeywordInteractor
import dev.cisnux.core.domain.usecases.MovieInteractor
import dev.cisnux.core.utils.MovieCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    movieUseCase: MovieInteractor,
    keywordUseCase: KeywordInteractor,
) : ViewModel() {
    private var query = MutableStateFlow("")
    private var isSearch = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedMovies = isSearch
        .asStateFlow()
        .flatMapLatest { isSearch ->
            val pagingMovies = if (isSearch) {
                query.asStateFlow().flatMapLatest {
                    movieUseCase.getMoviesByQuery(query = it)
                }
            } else flow {
                emit(PagingData.empty())
            }
            pagingMovies.cachedIn(scope = viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PagingData.empty()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val keywordSuggestions = query
        .asStateFlow()
        .flatMapLatest {
            keywordUseCase.getKeywordSuggestions(query = it)
                .cachedIn(scope = viewModelScope)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PagingData.empty()
        )

    val nowPlayingMovies = movieUseCase.getMoviesByCategory(
        category = MovieCategory.NOW_PLAYING_MOVIES
    ).cachedIn(scope = viewModelScope)

    val popularMovies = movieUseCase.getMoviesByCategory(
        category = MovieCategory.POPULAR_MOVIES
    ).cachedIn(scope = viewModelScope)

    val topRatedMovies = movieUseCase.getMoviesByCategory(
        category = MovieCategory.TOP_RATED_MOVIES
    ).cachedIn(scope = viewModelScope)

    fun updateQuery(newQuery: String) {
        query.value = newQuery
    }

    fun doSearch(newSearch: Boolean) {
        isSearch.value = newSearch
    }
}