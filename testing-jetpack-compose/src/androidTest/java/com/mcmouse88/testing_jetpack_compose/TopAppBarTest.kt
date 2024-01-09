package com.mcmouse88.testing_jetpack_compose

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.mcmouse88.testing_jetpack_compose.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTetRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest(): Unit = with(composeTetRule) {
        val allScreens = RallyScreen.entries.toList()
        setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = {},
                currentScreen = RallyScreen.Accounts
            )
        }

        onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists(): Unit = with(composeTetRule) {
        val allScreens = RallyScreen.entries.toList()
        setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = {},
                currentScreen = RallyScreen.Accounts
            )
        }

        onRoot().printToLog("currentLabelExists")

        onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertExists()
    }
}