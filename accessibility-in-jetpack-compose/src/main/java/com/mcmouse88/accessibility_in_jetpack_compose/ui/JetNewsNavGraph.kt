package com.mcmouse88.accessibility_in_jetpack_compose.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mcmouse88.accessibility_in_jetpack_compose.data.AppContainer
import com.mcmouse88.accessibility_in_jetpack_compose.ui.MainDestinations.ARTICLE_ID_KEY
import com.mcmouse88.accessibility_in_jetpack_compose.ui.article.ArticleScreen
import com.mcmouse88.accessibility_in_jetpack_compose.ui.home.HomeScreen
import com.mcmouse88.accessibility_in_jetpack_compose.ui.interests.InterestsScreen
import kotlinx.coroutines.launch

/**
 * Destinations used in the ([JetNewsApp]).
 */
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val INTERESTS_ROUTE = "interests"
    const val ARTICLE_ROUTE = "post"
    const val ARTICLE_ID_KEY = "postId"
}

@Composable
fun JetNewsNavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { scope.launch { scaffoldState.drawerState.open() } }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                postsRepository = appContainer.postsRepository,
                navigateToArticle = actions.navigateToArticle,
                openDrawer = openDrawer
            )
        }

        composable(MainDestinations.INTERESTS_ROUTE) {
            InterestsScreen(
                interestsRepository = appContainer.interestsRepository,
                openDrawer = openDrawer
            )
        }

        composable("${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ID_KEY}") { entry ->
            ArticleScreen(
                postId = entry.arguments?.getString(ARTICLE_ID_KEY),
                postsRepository = appContainer.postsRepository,
                onBack = actions.upPress
            )
        }
    }
}

/**
 * Models the navigation actions in the app
 */
class MainActions(
    navController: NavHostController
) {
    val navigateToArticle: (String) -> Unit = { postId: String ->
        navController.navigate("${MainDestinations.ARTICLE_ROUTE}/$postId")
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}