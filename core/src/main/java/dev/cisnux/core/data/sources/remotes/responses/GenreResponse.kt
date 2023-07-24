package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val id: Int,
    val name: String,
)