package com.mcmouse88.advanced_compose_state.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun Circle(color: Color) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawCircle(color)
        }
    )
}

@Composable
fun SemiRect(
    color: Color,
    lookingLeft: Boolean = true
) {
    val layoutDirection = LocalLayoutDirection.current
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            // The SemiRect should face left EITHER the lookingLeft params is true
            // OR the layoutDirection is Rtl
            val offset = if (lookingLeft xor (layoutDirection == LayoutDirection.Rtl)) {
                Offset(0f, 0f)
            } else {
                Offset(size.width / 2, 0f)
            }
            val size = Size(width = size.width / 2, height = size.height)

            drawRect(size = size, topLeft = offset, color = color)
        }
    )
}