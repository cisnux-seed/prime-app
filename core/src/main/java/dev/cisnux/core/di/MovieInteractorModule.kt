package dev.cisnux.core.di

import dev.cisnux.core.domain.usecases.MovieInteractor
import dev.cisnux.core.domain.usecases.MovieUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieInteractorModule {

    @Singleton
    @Binds
    abstract fun bindMovieInteractor(
        movieUseCase: MovieUseCase
    ): MovieInteractor
}