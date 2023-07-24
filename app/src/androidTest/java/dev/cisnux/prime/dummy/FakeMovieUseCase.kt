package dev.cisnux.prime.dummy

import androidx.paging.PagingData
import dev.cisnux.core.domain.models.Movie
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.models.MovieWidget
import dev.cisnux.core.domain.usecases.MovieInteractor
import dev.cisnux.core.utils.MovieCategory
import dev.cisnux.core.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface FakeMovieUseCase {
    object Success : MovieInteractor, FakeMovieUseCase {
        override fun getMoviesByCategory(category: MovieCategory): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.from(
                    data = dummyMovies,
                    sourceLoadStates = dummySuccessLoadState,
                    mediatorLoadStates = dummySuccessLoadState
                )
            )
        }

        override fun getMovieById(movieId: Int): Flow<UiState<MovieDetail>> = flow {
            emit(UiState.Loading)
            emit(UiState.Success(dummyMovieDetail))
        }

        override fun getRecommendationMovies(movieId: Int): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.from(
                    data = dummyMovies,
                    sourceLoadStates = dummySuccessLoadState,
                    mediatorLoadStates = dummySuccessLoadState
                )
            )
        }

        override fun getMoviesByQuery(query: String): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.from(
                    data = dummyMovies,
                    sourceLoadStates = dummySuccessLoadState,
                    mediatorLoadStates = dummySuccessLoadState
                )
            )
        }

        override suspend fun upsertFavoriteMovie(movie: MovieDetail) {}

        override fun getWatchlistMovies(): Flow<List<Movie>> = flow {}

        override fun getRandomWatchlistForWidget(): Flow<MovieWidget?> = flow {}
    }

    object Error : MovieInteractor, FakeMovieUseCase {
        override fun getMoviesByCategory(category: MovieCategory): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyConnectionFailureLoadState,
                    mediatorLoadStates = dummyConnectionFailureLoadState
                )
            )
        }

        override fun getMovieById(movieId: Int): Flow<UiState<MovieDetail>> = flow {
            emit(UiState.Loading)
            emit(UiState.Error(dummyConnectionFailure))
        }

        override fun getRecommendationMovies(movieId: Int): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyConnectionFailureLoadState,
                    mediatorLoadStates = dummyConnectionFailureLoadState
                )
            )
        }

        override fun getMoviesByQuery(query: String): Flow<PagingData<Movie>> = flow {
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyLoadingLoadState,
                    mediatorLoadStates = dummyLoadingLoadState
                )
            )
            emit(
                PagingData.empty(
                    sourceLoadStates = dummyConnectionFailureLoadState,
                    mediatorLoadStates = dummyConnectionFailureLoadState
                )
            )
        }

        override suspend fun upsertFavoriteMovie(movie: MovieDetail) {}

        override fun getWatchlistMovies(): Flow<List<Movie>> = flow {}
        override fun getRandomWatchlistForWidget(): Flow<MovieWidget?> = flow {}
    }
}