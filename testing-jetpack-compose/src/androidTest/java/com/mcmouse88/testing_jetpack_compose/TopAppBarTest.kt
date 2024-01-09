package com.mcmouse88.testing_jetpack_compose

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.mcmouse88.testing_jetpack_compose.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTetRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest() {
        with(composeTetRule) {
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
    }
}