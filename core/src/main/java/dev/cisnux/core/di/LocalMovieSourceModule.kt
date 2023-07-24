package dev.cisnux.core.di

import dev.cisnux.core.data.sources.locals.LocalMovieDataSource
import dev.cisnux.core.data.sources.locals.LocalMovieDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalMovieSourceModule {

    @Singleton
    @Binds
    abstract fun bindLocalMovieDataSource(
        localMovieDataSourceImpl: LocalMovieDataSourceImpl
    ): LocalMovieDataSource
}