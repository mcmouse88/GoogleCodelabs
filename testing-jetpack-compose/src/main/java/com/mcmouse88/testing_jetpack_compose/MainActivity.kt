package com.mcmouse88.testing_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mcmouse88.testing_jetpack_compose.ui.components.RallyTopAppBar
import com.mcmouse88.testing_jetpack_compose.ui.theme.TestingJetpackComposeTheme

// https://developer.android.com/codelabs/jetpack-compose-testing

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    TestingJetpackComposeTheme {
        val allScreens = RallyScreen.entries.toList()
        var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
        Scaffold(
            topBar = {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { currentScreen = it },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                currentScreen.Content(onScreenChange = { currentScreen = it })
            }
        }
    }
}