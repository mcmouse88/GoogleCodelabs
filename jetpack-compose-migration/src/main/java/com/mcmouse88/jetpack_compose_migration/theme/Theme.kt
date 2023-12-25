package com.mcmouse88.jetpack_compose_migration.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.mcmouse88.jetpack_compose_migration.R

@SuppressLint("ConflictingOnColor")
@Composable
fun SunflowerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors  = lightColors(
        primary = colorResource(id = R.color.sunflower_green_500),
        primaryVariant = colorResource(id = R.color.sunflower_green_700),
        secondary = colorResource(id = R.color.sunflower_yellow_500),
        background = colorResource(id = R.color.sunflower_green_500),
        onPrimary = colorResource(id = R.color.sunflower_black),
        onSecondary = colorResource(id = R.color.sunflower_black),
    )
    val darkColors  = darkColors(
        primary = colorResource(id = R.color.sunflower_green_100),
        primaryVariant = colorResource(id = R.color.sunflower_green_200),
        secondary = colorResource(id = R.color.sunflower_yellow_300),
        onPrimary = colorResource(id = R.color.sunflower_black),
        onSecondary = colorResource(id = R.color.sunflower_black),
        onBackground = colorResource(id = R.color.sunflower_black),
        surface = colorResource(id = R.color.sunflower_green_100_8pc_over_surface),
        onSurface = colorResource(id = R.color.sunflower_white),
    )

    val colors = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colors = colors,
        content = content
    )
}