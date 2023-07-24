package dev.cisnux.core.di

import dev.cisnux.core.data.sources.remotes.RemoteKeywordDataSource
import dev.cisnux.core.data.sources.remotes.RemoteKeywordDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteKeywordSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteMovieDataSource(
        remoteKeywordDataSourceImpl: RemoteKeywordDataSourceImpl
    ): RemoteKeywordDataSource
}