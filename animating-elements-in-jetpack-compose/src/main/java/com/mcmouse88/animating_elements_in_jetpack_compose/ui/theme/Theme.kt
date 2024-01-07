package com.mcmouse88.animating_elements_in_jetpack_compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatingElementInJetpackComposeTheme(
    content: @Composable () -> Unit
) {
    val colors = lightColorScheme(
        primary = Melon,
        primaryContainer = PaleDogwood,
        onPrimary = Color.Black,
        secondary = Peach,
        onSecondary = Color.Black
    )
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}