package com.mcmouse88.theming_compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ReplyProfileImage(
    drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = drawableResource),
        contentDescription = description,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
    )
}