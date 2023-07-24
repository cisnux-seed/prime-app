package dev.cisnux.favorite.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.EntryPointAccessors
import dev.cisnux.core.di.WatchlistMovieModuleDependencies
import dev.cisnux.favorite.di.DaggerWatchlistMovieComponent
import dev.cisnux.favorite.presentation.navigation.WatchlistMovieNavGraph
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import javax.inject.Inject

class WatchlistMovieActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerWatchlistMovieComponent
            .builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    WatchlistMovieModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setContent {
            PrimeTheme {
                WatchlistMovieNavGraph(factory = viewModelFactory)
            }
        }
    }
}