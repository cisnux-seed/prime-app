package dev.cisnux.favorite.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.cisnux.core.di.WatchlistMovieModuleDependencies
import dev.cisnux.favorite.presentation.WatchlistMovieActivity
import javax.inject.Singleton

@Component(
    dependencies = [WatchlistMovieModuleDependencies::class],
    modules = [WatchlistMovieViewModelModule::class]
)
@Singleton
interface WatchlistMovieComponent {
    fun inject(activity: WatchlistMovieActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(watchlistMovieModuleDependencies: WatchlistMovieModuleDependencies): Builder
        fun build(): WatchlistMovieComponent
    }
}