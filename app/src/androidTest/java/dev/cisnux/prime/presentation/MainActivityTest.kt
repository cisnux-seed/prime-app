package dev.cisnux.prime.presentation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.LargeTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidRule
import dev.cisnux.prime.presentation.utils.SplashWaitTimeMillis
import dev.cisnux.prime.DummyActivity
import dev.cisnux.prime.presentation.navigation.PrimeAppNavGraph
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import dev.cisnux.prime.presentation.utils.AppDestination
import org.junit.Before
import org.junit.Rule
import dev.cisnux.prime.R

@LargeTest
@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<DummyActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PrimeTheme {
                PrimeAppNavGraph(
                    navController = navController
                )
            }
        }
    }

    @Test
    fun splashScreen_NavigateToHomeScreen() {
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.app_name))
            .assertIsDisplayed()
        composeTestRule.waitUntil(SplashWaitTimeMillis) {
            val route = navController.currentBackStackEntry?.destination?.route
            AppDestination.HomeRoute.route == route
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addMovieToWatchlist_Success() {
        composeTestRule.waitUntil(SplashWaitTimeMillis) {
            val route = navController.currentBackStackEntry?.destination?.route
            AppDestination.HomeRoute.route == route
        }

        composeTestRule.waitForIdle()
        composeTestRule.waitUntilAtLeastOneExists(
            hasTestTag("movieCard")
        )
        composeTestRule.onNode(
            hasContentDescription(
                composeTestRule
                    .activity
                    .getString(R.string.now_playing_content_desc)
            )
        )
            .assertIsDisplayed()
            .performScrollToIndex(1)
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(AppDestination.MovieDetailRoute.route, route)
        composeTestRule.waitUntilExactlyOneExists(
            hasText(composeTestRule.activity.getString(R.string.add_to_watchlist)),
            timeoutMillis = 3000L
        )
        composeTestRule.onNode(hasText(composeTestRule.activity.getString(R.string.add_to_watchlist)))
            .assertIsDisplayed()
            .performClick()
        composeTestRule.onNode(hasText(composeTestRule.activity.getString(R.string.added_to_watchlist)))
            .assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription(composeTestRule.activity.getString(R.string.back_to_previous_page)))
            .performClick()
    }
}