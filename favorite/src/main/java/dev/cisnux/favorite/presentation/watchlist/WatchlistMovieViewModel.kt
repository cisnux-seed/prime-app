package dev.cisnux.favorite.presentation.watchlist

import androidx.lifecycle.ViewModel
import dev.cisnux.core.domain.usecases.MovieInteractor
import javax.inject.Inject

class WatchlistMovieViewModel @Inject constructor(
    useCase: MovieInteractor
): ViewModel() {
    val watchlistMovies = useCase.getWatchlistMovies()
}
