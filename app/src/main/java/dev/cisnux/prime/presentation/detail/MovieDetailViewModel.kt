package dev.cisnux.prime.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.usecases.MovieInteractor
import dev.cisnux.core.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: MovieInteractor,
) : ViewModel() {
    private val id = checkNotNull(value = savedStateHandle["id"]) as Int
    private val _movieDetailState = MutableStateFlow<UiState<MovieDetail>>(UiState.Initialize)
    val movieDetailState get() = _movieDetailState.asStateFlow()
    val recommendationMovies = useCase.getRecommendationMovies(movieId = id)

    init {
        getMovieDetailById()
    }

    fun getMovieDetailById() = viewModelScope.launch {
        _movieDetailState.value = UiState.Initialize
        useCase.getMovieById(movieId = id)
            .collectLatest {
                _movieDetailState.value = it
            }
    }

    fun onWatchlistChanged(movieDetail: MovieDetail) = viewModelScope.launch {
        useCase.upsertFavoriteMovie(movie = movieDetail)
    }
}