package com.mcmouse88.testing_jetpack_compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mcmouse88.testing_jetpack_compose.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun overviewScreen_alertDisplayed(): Unit = with(composeTestRule) {
        setContent {
            OverviewBody()
        }

        onNodeWithText("Alerts")
            .assertIsDisplayed()
    }
}