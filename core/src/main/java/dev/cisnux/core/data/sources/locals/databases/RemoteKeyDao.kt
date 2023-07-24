package dev.cisnux.core.data.sources.locals.databases

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.cisnux.core.utils.REMOTE_KEY_TABLE

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsertRemoteKeys(remoteKeys: List<MovieRemoteKeyEntity>)

    @Query("SELECT * FROM $REMOTE_KEY_TABLE WHERE id = :id")
    suspend fun getRemoteKeyById(id: Int): MovieRemoteKeyEntity?

    @Query("SELECT created_at FROM $REMOTE_KEY_TABLE ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}