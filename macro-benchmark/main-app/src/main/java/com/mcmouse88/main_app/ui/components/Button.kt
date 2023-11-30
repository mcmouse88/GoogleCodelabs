package com.mcmouse88.main_app.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme

@Composable
fun BenchMarkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ButtonShape,
    border: BorderStroke? = null,
    backgroundGradient: List<Color> = BenchMarkTheme.colors.interactivePrimary,
    disabledBackgroundGradient: List<Color> = BenchMarkTheme.colors.interactiveSecondary,
    contentColor: Color = BenchMarkTheme.colors.textInteractive,
    disabledContentColor: Color = BenchMarkTheme.colors.textHelp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    BenchMarkSurface(
        shape = shape,
        color = Color.Transparent,
        contentColor = if (enabled) contentColor else disabledContentColor,
        border = border,
        modifier = modifier
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colors = if (enabled) backgroundGradient else disabledBackgroundGradient
                )
            )
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Row(
                modifier = Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .indication(interactionSource, rememberRipple())
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

private val ButtonShape = RoundedCornerShape(percent = 50)

@Preview(name = "default", group = "round")
@Preview(name = "dark theme", group = "round", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "large font", group = "round", fontScale = 2f)
@Composable
fun ButtonPreview() {
    BenchMarkTheme {
        BenchMarkButton(onClick = {}) {
            Text(text = "Demo")
        }
    }
}

@Preview(name = "default", group = "rectangle")
@Preview(name = "dark theme", group = "rectangle", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "large font", group = "rectangle", fontScale = 2f)
@Composable
fun RectangleButtonPreview() {
    BenchMarkTheme {
        BenchMarkButton(onClick = {}, shape = RectangleShape) {
            Text(text = "Demo")
        }
    }
}