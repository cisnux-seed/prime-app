package dev.cisnux.core.domain.models

import android.graphics.Bitmap

data class MovieWidget(
    val id: Int,
    val title: String,
    val poster: Bitmap,
    val overview: String,
)