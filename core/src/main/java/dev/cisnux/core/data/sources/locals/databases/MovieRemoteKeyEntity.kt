package dev.cisnux.core.data.sources.locals.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.cisnux.core.utils.REMOTE_KEY_TABLE

@Entity(tableName = REMOTE_KEY_TABLE)
data class MovieRemoteKeyEntity(
    @PrimaryKey
    val id: Int,
    val nowPlayingPrevKey: Int?,
    val nowPlayingNextKey: Int?,
    val popularPrevKey: Int?,
    val popularNextKey: Int?,
    val topRatedPrevKey: Int?,
    val topRatedNextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
