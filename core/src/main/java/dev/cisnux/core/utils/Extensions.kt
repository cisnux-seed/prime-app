package dev.cisnux.core.utils

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import dev.cisnux.core.data.sources.locals.databases.MovieEntity
import dev.cisnux.core.data.sources.remotes.responses.MovieResponseItem
import dev.cisnux.core.domain.models.MovieDetail
import dev.cisnux.core.domain.models.Movie

fun MovieDetail.asMovieEntity() =
    MovieEntity(
        id = id,
        title = title,
        poster = poster,
        isFavorite = isFavorite,
        overview = overview,
        movieCategory = movieCategory,
        nowPlayingPage = nowPlayingPage,
        popularPage = popularPage,
        topRatedPage = topRatedPage
    )

fun List<MovieEntity>.fromListMovieEntities() = map {
    Movie(
        id = it.id,
        title = it.title,
        poster = it.poster,
        overview = it.overview
    )
}

fun PagingData<MovieEntity>.fromPagingMovieEntities() = map {
    Movie(
        id = it.id,
        title = it.title,
        poster = it.poster,
        overview = it.overview
    )
}

fun PagingData<MovieResponseItem>.fromPagingMovieResponseItem() =
    filter { it.posterPath != null && it.overview?.isNotBlank() == true }
        .map {
            Movie(
                id = it.id,
                title = it.title,
                poster = it.posterPath!!,
                overview = it.overview!!
            )
        }

val String.originalImage get() = "$ORIGINAL_IMAGE_URL$this"

val String.w500Image get() = "$W500_IMAGE_URL$this"

fun Int.duration(): String {
    val hours = this / 60
    val minutes = this % 60

    return if (hours > 0)
        "${hours}h ${minutes}m"
    else "${minutes}m"
}

fun Float.toRound() = (this * 10).toInt() / 10f