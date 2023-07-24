package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val results: List<MovieResponseItem>
)