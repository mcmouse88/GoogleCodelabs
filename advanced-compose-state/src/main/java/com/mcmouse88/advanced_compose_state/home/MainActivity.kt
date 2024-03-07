package com.mcmouse88.advanced_compose_state.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.mcmouse88.advanced_compose_state.details.launchDetailsActivity
import com.mcmouse88.advanced_compose_state.ui.theme.AdvancedStateComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AdvancedStateComposeTheme {
                MainScreen(
                    onExploreItemClicked = {
                        launchDetailsActivity(
                            context = this,
                            item = it
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    onExploreItemClicked: OnExploreItemClicked
) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        CraneHome(onExploreItemClicked = onExploreItemClicked)
    }
}