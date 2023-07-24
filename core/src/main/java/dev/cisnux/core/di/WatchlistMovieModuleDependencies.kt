package dev.cisnux.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.core.domain.usecases.MovieInteractor

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WatchlistMovieModuleDependencies {
    fun movieInteractor(): MovieInteractor
}