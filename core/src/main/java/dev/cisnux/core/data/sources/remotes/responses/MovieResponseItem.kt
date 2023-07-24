package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseItem(
    val id: Int,
    val title: String,
    @SerialName(value = "poster_path")
    val posterPath: String?,
    val overview: String?,
)
