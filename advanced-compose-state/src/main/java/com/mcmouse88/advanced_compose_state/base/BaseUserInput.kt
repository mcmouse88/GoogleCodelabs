package com.mcmouse88.advanced_compose_state.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcmouse88.advanced_compose_state.R
import com.mcmouse88.advanced_compose_state.ui.theme.AdvancedStateComposeTheme
import com.mcmouse88.advanced_compose_state.ui.theme.captionTextStyle
import com.mcmouse88.advanced_compose_state.ui.theme.crane_disable_icon

@Composable
fun SimpleUserInput(
    text: String? = null,
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null
) {
    CraneUserInput(
        caption = if (text == null) caption else null,
        text = text.orEmpty(),
        vectorImageId = vectorImageId
    )
}

@Composable
fun CraneUserInput(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    tint: Color = LocalContentColor.current
) {
    CraneBaseUserInput(
        modifier = modifier,
        onClick = onClick,
        caption = caption,
        vectorImageId = vectorImageId,
        tintIcon = { text.isNotEmpty() },
        tint = tint
    ) {
        Text(text = text, style = MaterialTheme.typography.body1.copy(color = tint))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CraneBaseUserInput(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    showCaption: () -> Boolean = { true },
    tintIcon: () -> Boolean,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = MaterialTheme.colors.primaryVariant
    ) {
        Row(
            modifier = Modifier.padding(all = 12.dp)
        ) {
            if (vectorImageId != null) {
                Icon(
                    painter = painterResource(id = vectorImageId),
                    contentDescription = null,
                    tint = if (tintIcon.invoke()) tint else crane_disable_icon,
                    modifier = Modifier.size(24.dp, 24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (caption != null && showCaption()) {
                Text(
                    text = caption,
                    style = (captionTextStyle).copy(color = tint),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                content.invoke()
            }
        }
    }
}

@Preview
@Composable
fun PreviewInput() {
    AdvancedStateComposeTheme {
        Surface {
            CraneBaseUserInput(
                caption = "Caption",
                vectorImageId = R.drawable.ic_plane,
                tintIcon = { true },
                showCaption = { true }
            ) {
                Text(text = "text", style = MaterialTheme.typography.body1)
            }
        }
    }
}