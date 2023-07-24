package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    val id: Int,
    val title: String,
    @SerialName(value = "poster_path")
    val posterPath: String,
    @SerialName(value = "vote_average")
    val voteAverage: Float,
    val overview: String,
    val runtime: Int,
    val genres: List<GenreResponse>
)