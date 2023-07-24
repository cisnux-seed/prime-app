package dev.cisnux.core.di

import dev.cisnux.core.data.repositories.KeywordRepositoryImpl
import dev.cisnux.core.domain.repositories.KeywordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KeywordRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindKeywordRepository(
        keywordRepositoryImpl: KeywordRepositoryImpl
    ): KeywordRepository
}