package dev.cisnux.prime.presentation.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.utils.SplashWaitTimeMillis
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    PrimeTheme {
        SplashScreen(navigateToHome = { /*TODO*/ })
    }
}

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resId = R.raw.splash_screen))
    val onNavigation by rememberUpdatedState(navigateToHome)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(SplashWaitTimeMillis)
        onNavigation()
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.app_name)
            }
        )
    }
}
