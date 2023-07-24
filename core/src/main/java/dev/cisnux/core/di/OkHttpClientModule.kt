package dev.cisnux.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.core.BuildConfig
import dev.cisnux.core.utils.TMDB_PINNER
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {
    private const val CERTIFICATE_PINNER = BuildConfig.CERTIFICATE_PINNING

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder()
            .add(pattern = TMDB_PINNER, CERTIFICATE_PINNER)
            .build()
        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()
    }
}