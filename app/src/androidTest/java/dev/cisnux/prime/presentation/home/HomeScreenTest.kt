package dev.cisnux.prime.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.MediumTest
import dev.cisnux.prime.dummy.FakeKeywordUseCase
import dev.cisnux.prime.dummy.FakeMovieUseCase
import dev.cisnux.prime.dummy.dummyConnectionFailure
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import org.junit.Rule
import org.junit.Test
import dev.cisnux.prime.R

@MediumTest
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeScreen_shouldDisplayMovieCardWhenSuccess() {
        val homeViewModel = HomeViewModel(
            movieUseCase = FakeMovieUseCase.Success,
            keywordUseCase = FakeKeywordUseCase.Success
        )

        composeTestRule.setContent {
            PrimeTheme {
                HomeScreen(
                    navigateToMovieDetail = {},
                    navigateForBottomNav = { _, _ -> },
                    homeViewModel = homeViewModel
                )
            }
        }

        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.now_playing)),
        ).assertIsDisplayed()
        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.popular)),
        ).assertIsDisplayed()
        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.top_rated)),
        ).assertIsDisplayed()

        composeTestRule.onAllNodes(
            hasTestTag("movieCardPlaceholder"),
            useUnmergedTree = true
        )[0].assertIsDisplayed()

        composeTestRule.waitForIdle()
        composeTestRule.onAllNodes(
            hasTestTag("movieCard"),
        )[0].assertIsDisplayed()
    }

    @Test
    fun homeScreen_shouldDisplaySnackBarWhenError() {
        val homeViewModel = HomeViewModel(
            movieUseCase = FakeMovieUseCase.Error,
            keywordUseCase = FakeKeywordUseCase.Success
        )

        composeTestRule.setContent {
            PrimeTheme {
                HomeScreen(
                    navigateToMovieDetail = {},
                    navigateForBottomNav = { _, _ -> },
                    homeViewModel = homeViewModel
                )
            }
        }

        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.now_playing)),
        ).assertIsDisplayed()
        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.popular)),
        ).assertIsDisplayed()
        composeTestRule.onNode(
            hasText(composeTestRule.activity.getString(R.string.top_rated)),
        ).assertIsDisplayed()

        composeTestRule.onAllNodes(
            hasTestTag("movieCardPlaceholder"),
            useUnmergedTree = true
        )[0].assertIsDisplayed()

        composeTestRule.waitForIdle()
        composeTestRule.onAllNodes(
            hasText(dummyConnectionFailure.message!!),
        )[0].assertIsDisplayed()
    }
}