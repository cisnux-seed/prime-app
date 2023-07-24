package dev.cisnux.prime.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.ui.theme.md_theme_dark_yellow
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    starsColor: Color = if (isSystemInDarkTheme()) Color.Yellow
    else md_theme_dark_yellow,
    stars: Int = 5,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0f))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
        if (halfStar) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_half),
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.TwoTone.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingHalfPreview() {
    RatingBar(stars = 1, rating = 0.5f)
}

@Preview(showBackground = true)
@Composable
private fun RatingFullPreview() {
    RatingBar(stars = 5, rating = 5.0f)
}

@Preview(showBackground = true)
@Composable
private fun RatingPreview() {
    RatingBar(stars = 5, rating = 3.5f)
}