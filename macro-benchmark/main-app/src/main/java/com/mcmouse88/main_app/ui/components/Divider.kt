package com.mcmouse88.main_app.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme

private const val DividerAlpha = 0.12f

@Composable
fun BenchMarkDivider(
    modifier: Modifier = Modifier,
    color: Color = BenchMarkTheme.colors.uiBorder.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

@Preview("default", showBackground = true)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DividerPreview() {
    BenchMarkTheme {
        Box(
            modifier =  Modifier.size(height = 10.dp, width = 100.dp)
        ) {
            BenchMarkDivider(modifier = Modifier.align(Alignment.Center))
        }
    }
}