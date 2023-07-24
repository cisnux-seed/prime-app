package dev.cisnux.core.di

import dev.cisnux.core.data.sources.remotes.RemoteMovieDataSource
import dev.cisnux.core.data.sources.remotes.RemoteMovieDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteMovieSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteMovieDataSource(
        remoteMovieDataSourceImpl: RemoteMovieDataSourceImpl
    ): RemoteMovieDataSource
}