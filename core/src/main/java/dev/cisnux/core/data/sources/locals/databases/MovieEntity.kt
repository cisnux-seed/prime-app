package dev.cisnux.core.data.sources.locals.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.cisnux.core.utils.MOVIE_TABLE

@Entity(tableName = MOVIE_TABLE)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster: String,
    val overview: String = "",
    @ColumnInfo(name = "movie_category")
    val movieCategory: String? = null,
    @ColumnInfo(name = "favorite_movie")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "now_playing_page")
    val nowPlayingPage: Int? = null,
    @ColumnInfo(name = "popular_page")
    val popularPage: Int? = null,
    @ColumnInfo(name = "top_rated_page")
    val topRatedPage: Int? = null,
)