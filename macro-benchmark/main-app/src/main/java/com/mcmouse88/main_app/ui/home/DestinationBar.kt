package com.mcmouse88.main_app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcmouse88.main_app.R
import com.mcmouse88.main_app.ui.components.BenchMarkDivider
import com.mcmouse88.main_app.ui.theme.AlphaNearOpaque
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme

@Composable
fun DestinationBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.statusBarsPadding()
    ) {
        TopAppBar(
            backgroundColor = BenchMarkTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = BenchMarkTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Text(
                text = stringResource(R.string.delivery_to_amphitheater_way),
                style = MaterialTheme.typography.subtitle1,
                color = BenchMarkTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = stringResource(id = R.string.label_select_delivery),
                    tint = BenchMarkTheme.colors.brand
                )
            }
        }
        BenchMarkDivider()
    }
}

@Preview(name = "default")
@Preview(name = "dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "large font", fontScale = 2f)
@Composable
private fun DestinationBarPreview() {
    BenchMarkTheme {
        DestinationBar()
    }
}