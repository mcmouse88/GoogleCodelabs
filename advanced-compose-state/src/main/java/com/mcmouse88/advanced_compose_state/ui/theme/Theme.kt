package com.mcmouse88.advanced_compose_state.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val craneColors = lightColors(
    primary = crane_purple_800,
    secondary = crane_red,
    surface = crane_purple_900,
    onSurface = crane_white,
    primaryVariant = crane_purple_700
)

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

@Composable
fun AdvancedStateComposeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = craneColors,
        typography = craneTypography,
        content = content
    )
}