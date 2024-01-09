package com.mcmouse88.testing_jetpack_compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
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

        onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        onNode(
            hasText(RallyScreen.Accounts.name.uppercase()) and
                    hasParent(
                        hasContentDescription(RallyScreen.Accounts.name)
                    ),
            useUnmergedTree = true
        ).assertExists()
    }

    @Test
    fun rallyTopAppBarTest_clickChangeSection(): Unit = with(composeTetRule) {
        val allScreens = RallyScreen.entries.toList()
        setContent {
            var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { currentScreen = it },
                currentScreen = currentScreen
            )
        }

        onNode(
            hasText(RallyScreen.Overview.name.uppercase()) and
            hasParent(
                hasContentDescription(RallyScreen.Overview.name)
            ),
            useUnmergedTree = true
        ).assertExists()

        onNodeWithContentDescription(RallyScreen.Accounts.name)
            .performClick()

        onNode(
            hasText(RallyScreen.Accounts.name.uppercase()) and
                    hasParent(
                        hasContentDescription(RallyScreen.Accounts.name)
                    ),
            useUnmergedTree = true
        ).assertExists()
    }
}