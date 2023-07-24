package dev.cisnux.core.di

import dev.cisnux.core.domain.usecases.KeywordInteractor
import dev.cisnux.core.domain.usecases.KeywordUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KeywordInteractorModule {

    @Singleton
    @Binds
    abstract fun bindKeywordInteractor(
        keywordUseCase: KeywordUseCase
    ): KeywordInteractor
}