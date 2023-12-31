package com.mcmouse88.testing_jetpack_compose.ui.overview

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mcmouse88.testing_jetpack_compose.R
import com.mcmouse88.testing_jetpack_compose.RallyScreen
import com.mcmouse88.testing_jetpack_compose.data.UserData
import com.mcmouse88.testing_jetpack_compose.ui.components.AccountRow
import com.mcmouse88.testing_jetpack_compose.ui.components.BillRow
import com.mcmouse88.testing_jetpack_compose.ui.components.RallyAlertDialog
import com.mcmouse88.testing_jetpack_compose.ui.components.RallyDivider
import com.mcmouse88.testing_jetpack_compose.ui.components.formatAmount
import com.mcmouse88.testing_jetpack_compose.ui.theme.TestingJetpackComposeTheme
import java.util.Locale

private val RallyDefaultPadding = 12.dp
private const val SHOWN_ITEMS = 3

@Composable
fun OverviewBody(
    onScreenChange: (RallyScreen) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AlertCard()
        Spacer(modifier = Modifier.height(RallyDefaultPadding))
        AccountsCard(onScreenChange)
        Spacer(modifier = Modifier.height(RallyDefaultPadding))
        BillsCard(onScreenChange)
    }
}

/**
 * The Alerts card within the Rally Overview screen.
 */
@Composable
private fun AlertCard() {
    var showDialog by remember { mutableStateOf(false) }
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month."

    if (showDialog) {
        RallyAlertDialog(
            onDismiss = { showDialog = false },
            bodyText = alertMessage,
            buttonText = "Dismiss".uppercase(Locale.getDefault())
        )
    }

    val infiniteElevationAnimation = rememberInfiniteTransition(label = "Infinite Elevation")
    val animatedElevation by infiniteElevationAnimation.animateValue(
        initialValue = 1.dp,
        targetValue = 8.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Animated Elevation"
    )

    Card(
        elevation = animatedElevation
    ) {
        Column {
            AlertHeader {
                showDialog = true
            }
            RallyDivider(
                modifier = Modifier.padding(horizontal = RallyDefaultPadding)
            )
            AlertItem(alertMessage)
        }
    }
}

/**
 * The Bills card within the Rally Overview screen.
 */
@Composable
private fun BillsCard(
    onScreenChange: (RallyScreen) -> Unit
) {
    val amount = UserData.bills.map { it.amount }.sum()
    OverViewScreenCard(
        title = stringResource(id = R.string.bills),
        amount = amount,
        onClickSeeAll = {
            onScreenChange.invoke(RallyScreen.Bills)
        },
        values = { it.amount },
        colors = { it.color },
        data = UserData.bills
    ) { bill ->
        BillRow(
            name = bill.name,
            due = bill.due,
            amount = bill.amount,
            color = bill.color
        )
    }
}

@Composable
fun AlertHeader(
    onClickSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(RallyDefaultPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Alerts",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        TextButton(
            onClick = onClickSeeAll,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "SEE ALL",
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
private fun AlertItem(message: String) {
    Row(
        modifier = Modifier
            .padding(RallyDefaultPadding)
            // Regard the whole row as one semantics node. This way each row will receive focus as
            // a whole and the focus bounds will be around the whole row content. The semantics
            // properties of the descendants will be merged. If we'd use clearAndSetSemantics instead,
            // we'd have to define the semantics properties explicitly.
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.Top)
                .clearAndSetSemantics {}
        ) {
            Icon(imageVector = Icons.Filled.Sort, contentDescription = null)
        }
    }
}

/**
 * The Accounts card within the Rally Overview screen.
 */
@Composable
private fun AccountsCard(
    onScreenChange: (RallyScreen) -> Unit
) {
    val amount = UserData.accounts.map { it.balance }.sum()
    OverViewScreenCard(
        title = stringResource(id = R.string.accounts),
        amount = amount,
        onClickSeeAll = {
            onScreenChange(RallyScreen.Accounts)
        },
        data = UserData.accounts,
        colors = { it.color },
        values = { it.balance }
    ) { account ->
        AccountRow(
            name = account.name,
            number = account.number,
            amount = account.balance,
            color = account.color
        )
    }
}

/**
 * Base structure for cards in the Overview screen.
 */
@Composable
private fun <T> OverViewScreenCard(
    title: String,
    amount: Float,
    onClickSeeAll: () -> Unit,
    values: (T) -> Float,
    colors: (T) -> Color,
    data: List<T>,
    row: @Composable (T) -> Unit
) {
    Card {
        Column {
            Column(
                modifier = Modifier.padding(RallyDefaultPadding)
            ) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
                val amountText = "$" + formatAmount(amount)
                Text(text = amountText, style = MaterialTheme.typography.h2)
            }
            OverViewDivider(
                data = data,
                values = values,
                colors = colors
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)
            ) {
                data.take(SHOWN_ITEMS).forEach { row.invoke(it) }
                SeeAllButton(onClickSeeAll)
            }
        }
    }
}

@Composable
private fun <T> OverViewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: (T) -> Color
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        data.forEach { item: T ->
            Spacer(
                modifier = Modifier
                    .weight(values(item))
                    .height(1.dp)
                    .background(colors(item))
            )
        }
    }
}

@Composable
private fun SeeAllButton(
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.see_all))
    }
}

@Preview
@Composable
fun AlertCardPreview() {
    TestingJetpackComposeTheme {
        OverviewBody()
    }
}