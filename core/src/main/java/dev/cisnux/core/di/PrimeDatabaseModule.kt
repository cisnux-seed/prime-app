package dev.cisnux.core.di

import android.content.Context
import androidx.room.Room
import dev.cisnux.core.utils.CISNUX_PRIME_DB
import dev.cisnux.core.data.sources.locals.databases.PrimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.cisnux.core.BuildConfig
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrimeDatabaseModule {
    private const val PASSPHRASE = BuildConfig.PASSPHRASE

    @Singleton
    @Provides
    fun providePrimeDatabase(
        @ApplicationContext applicationContext: Context
    ): PrimeDatabase {
        val factory = SupportFactory(PASSPHRASE.toByteArray())
        val primeDatabaseBuilder = Room.databaseBuilder(
            applicationContext,
            PrimeDatabase::class.java,
            CISNUX_PRIME_DB
        )
        return primeDatabaseBuilder
            .openHelperFactory(factory)
            .build()
    }
}