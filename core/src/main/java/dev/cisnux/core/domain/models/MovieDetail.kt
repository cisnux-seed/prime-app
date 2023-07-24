package dev.cisnux.core.domain.models

data class MovieDetail(
    val id:Int,
    val title: String,
    val poster: String,
    val overview: String,
    val duration: String,
    val voteAverage: Float,
    val genres: String,
    val isFavorite: Boolean,
    val movieCategory: String? = null,
    val nowPlayingPage: Int? = null,
    val popularPage: Int? = null,
    val topRatedPage: Int? = null,
)