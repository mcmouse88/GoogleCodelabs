package com.mcmouse88.advanced_compose_state.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import com.mcmouse88.advanced_compose_state.R
import com.mcmouse88.advanced_compose_state.home.CraneScreen

@Composable
fun CraneTabBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
    children: @Composable (Modifier) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        // Separate Row as the children shouldn't have the padding
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = stringResource(id = R.string.cd_menu),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable(onClick = onMenuClicked)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_crane_logo),
                contentDescription = null
            )
        }

        children.invoke(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun CraneTabs(
    titles: List<String>,
    tabSelected: CraneScreen,
    modifier: Modifier = Modifier,
    onTabSelected: (CraneScreen) -> Unit
) {
    TabRow(
        selectedTabIndex = tabSelected.ordinal,
        contentColor = MaterialTheme.colors.onSurface,
        modifier = modifier,
        indicator = {},
        divider = {}
    ) {
        titles.forEachIndexed { index, title ->
            val selected = index == tabSelected.ordinal

            var textModifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            if (selected) {
                textModifier = Modifier
                    .border(
                        border = BorderStroke(width = 2.dp, color = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(textModifier)
            }

            Tab(
                selected = selected,
                onClick = { onTabSelected.invoke(CraneScreen.entries[index]) })
            {
                Text(
                    text = title.uppercase(
                        ConfigurationCompat.getLocales(LocalConfiguration.current)[0]!!
                    ),
                    modifier = textModifier
                )
            }
        }
    }
}