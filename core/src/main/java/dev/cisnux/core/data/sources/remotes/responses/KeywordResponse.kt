package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.Serializable

@Serializable
data class KeywordResponse(
    val results: List<KeywordResponseItem>
)
