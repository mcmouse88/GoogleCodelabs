package com.mcmouse88.testing_jetpack_compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.mcmouse88.testing_jetpack_compose.data.UserData
import com.mcmouse88.testing_jetpack_compose.ui.overview.OverviewBody
import com.mcmouse88.testing_jetpack_compose.ui.accounts.AccountsBody
import com.mcmouse88.testing_jetpack_compose.ui.bills.BillsBody

/**
 * Screen state for Rally. Navigation is kept simple until a proper mechanism is available. Back
 * navigation is not supported.
 */
enum class RallyScreen(
    val icon: ImageVector,
    private val body: @Composable ((RallyScreen) -> Unit) -> Unit
) {
    Overview(
        icon = Icons.Filled.PieChart,
        body = { onScreenChange -> OverviewBody(onScreenChange) }
    ),
    Accounts(
        icon = Icons.Filled.AttachMoney,
        body = { AccountsBody(UserData.accounts) }
    ),
    Bills(
        icon = Icons.Filled.MoneyOff,
        body = { BillsBody(bills = UserData.bills) }
    );

    @Composable
    fun Content(onScreenChange: (RallyScreen) -> Unit) {
        body(onScreenChange)
    }
}