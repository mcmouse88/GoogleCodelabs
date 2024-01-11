package com.mcmouse88.animating_elements_in_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mcmouse88.animating_elements_in_jetpack_compose.ui.theme.AnimatingElementInJetpackComposeTheme
import com.mcmouse88.animating_elements_in_jetpack_compose.ui.theme.home.Home

// https://developer.android.com/codelabs/jetpack-compose-animation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatingElementInJetpackComposeTheme {
                Home()
            }
        }
    }
}