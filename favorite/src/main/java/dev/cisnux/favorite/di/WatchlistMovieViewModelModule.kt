package dev.cisnux.favorite.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoMap
import dev.cisnux.core.factory.ViewModelFactory
import dev.cisnux.favorite.presentation.watchlist.WatchlistMovieViewModel

@Module
@InstallIn(ViewModelComponent::class)
abstract class WatchlistMovieViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @WatchlistMovieViewModelKey(WatchlistMovieViewModel::class)
    abstract fun bindFavoriteMovieViewModel(viewModel: WatchlistMovieViewModel): ViewModel
}