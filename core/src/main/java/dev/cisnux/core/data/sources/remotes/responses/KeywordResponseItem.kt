package dev.cisnux.core.data.sources.remotes.responses

import kotlinx.serialization.Serializable

@Serializable
data class KeywordResponseItem(
    val id: Int,
    val name: String,
)
