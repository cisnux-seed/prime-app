package dev.cisnux.prime.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.prime.presentation.navigation.PrimeAppNavGraph
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimeTheme {
                PrimeAppNavGraph()
            }
        }
    }
}