package dev.cisnux.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object QueryUtil {
    fun sortedQuery(movieCategory: MovieCategory) = SimpleSQLiteQuery(
        query = "SELECT * FROM $MOVIE_TABLE WHERE movie_category LIKE '%' || ? || '%' ${
            when (movieCategory) {
                MovieCategory.NOW_PLAYING_MOVIES -> "ORDER BY now_playing_page"
                MovieCategory.POPULAR_MOVIES -> "ORDER BY popular_page"
                MovieCategory.TOP_RATED_MOVIES -> "ORDER BY top_rated_page"
            }
        }",
        bindArgs = arrayOf(movieCategory.value)
    )
}