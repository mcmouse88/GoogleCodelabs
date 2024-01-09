package com.mcmouse88.testing_jetpack_compose.ui.accounts

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mcmouse88.testing_jetpack_compose.R
import com.mcmouse88.testing_jetpack_compose.data.Account
import com.mcmouse88.testing_jetpack_compose.ui.components.AccountRow
import com.mcmouse88.testing_jetpack_compose.ui.components.StatementBody

@Composable
fun AccountsBody(
    accounts: List<Account>
) {
    StatementBody(
        items = accounts,
        amounts = { it.balance },
        colors = { it.color },
        amountsTotal = accounts.map { it.balance }.sum(),
        circleLabel = stringResource(id = R.string.total),
        rows = { account ->
            AccountRow(
                name = account.name,
                number = account.number,
                amount = account.balance,
                color = account.color
            )
        }
    )
}