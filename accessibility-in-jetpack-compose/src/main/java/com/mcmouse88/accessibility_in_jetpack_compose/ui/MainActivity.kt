package com.mcmouse88.accessibility_in_jetpack_compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.mcmouse88.accessibility_in_jetpack_compose.JetNewsApplication
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme

// https://developer.android.com/codelabs/jetpack-compose-accessibility

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appContainer = (application as JetNewsApplication).container
        setContent {
            AccessibilityInJetpackComposeTheme {
                JetNewsApp(appContainer = appContainer)
            }
        }
    }
}