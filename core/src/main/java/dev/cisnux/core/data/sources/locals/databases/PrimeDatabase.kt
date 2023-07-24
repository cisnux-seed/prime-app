package dev.cisnux.core.data.sources.locals.databases

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, MovieRemoteKeyEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class PrimeDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}