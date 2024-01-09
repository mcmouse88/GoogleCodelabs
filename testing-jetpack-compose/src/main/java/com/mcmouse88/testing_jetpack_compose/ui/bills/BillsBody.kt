package com.mcmouse88.testing_jetpack_compose.ui.bills

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mcmouse88.testing_jetpack_compose.R
import com.mcmouse88.testing_jetpack_compose.data.Bill
import com.mcmouse88.testing_jetpack_compose.ui.components.BillRow
import com.mcmouse88.testing_jetpack_compose.ui.components.StatementBody

@Composable
fun BillsBody(
    bills: List<Bill>
) {
    StatementBody(
        items = bills,
        colors = { it.color },
        amounts = { it.amount },
        amountsTotal = bills.map { it.amount }.sum(),
        circleLabel = stringResource(id = R.string.due),
        rows = { bill ->
            BillRow(
                name = bill.name,
                due = bill.due,
                amount = bill.amount,
                color = bill.color
            )
        }
    )
}