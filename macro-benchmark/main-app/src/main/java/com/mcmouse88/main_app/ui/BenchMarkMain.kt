package com.mcmouse88.main_app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mcmouse88.main_app.ui.components.BenchMarkScaffold
import com.mcmouse88.main_app.ui.components.BenchMarkSnackbar
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BenchMarkMain() {
    BenchMarkTheme {
        val appState = rememberBenchMarkAppState()
        BenchMarkScaffold(
            modifier = Modifier.semantics {
                // Allows to use testTag() for UiAutomator resource-id.
                testTagsAsResourceId = true
            },
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    BenchMarkBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute,
                        navigateToRoute = appState::navigateToBottomBarRoute
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> BenchMarkSnackbar(snackbarData = snackbarData) }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                benchMarkNavGraph(
                    onSnackSelected = appState::navigateToSnackDetail,
                    upPress = appState::pressUp
                )
            }
        }
    }
}

private fun NavGraphBuilder.benchMarkNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected)
    }

    composable(
        route = "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) {
            type = NavType.LongType
        })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(snackId, upPress)
    }
}