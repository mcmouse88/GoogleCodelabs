package com.mcmouse88.advanced_compose_state.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mcmouse88.advanced_compose_state.R

@Composable
fun LandingScreen(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // TODO: Codelab LaunchEffect and rememberUpdateState() step
        // TODO: Make LandingScreen disappear after loading data
        Image(
            painter = painterResource(id = R.drawable.ic_crane_drawer),
            contentDescription = null
        )
    }
}