package com.mcmouse88.advanced_compose_state.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mcmouse88.advanced_compose_state.R
import com.mcmouse88.advanced_compose_state.base.CraneDrawer
import com.mcmouse88.advanced_compose_state.base.CraneTabBar
import com.mcmouse88.advanced_compose_state.base.CraneTabs
import com.mcmouse88.advanced_compose_state.base.ExploreSection
import com.mcmouse88.advanced_compose_state.data.ExploreModel
import kotlinx.coroutines.launch

typealias OnExploreItemClicked = (ExploreModel) -> Unit

enum class CraneScreen {
    Fly, Sleep, Eat
}

@Composable
fun CraneHome(
    onExploreItemClicked: OnExploreItemClicked,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        drawerContent = {
            CraneDrawer()
        },
        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding()
    ) { paddings ->
        val scope = rememberCoroutineScope()
        CraneHomeContent(
            onExploreItemClicked = onExploreItemClicked,
            modifier = modifier.padding(paddings),
            openDrawer = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CraneHomeContent(
    onExploreItemClicked: OnExploreItemClicked,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val suggestedDestinations by viewModel.suggestedDestinations.collectAsStateWithLifecycle()

    val onPeopleChanged: (Int) -> Unit = { viewModel.updatePeople(it) }
    var tabSelected by remember { mutableStateOf(CraneScreen.Fly) }

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        appBar = {
            HomeTabBar(
                openDrawer = openDrawer,
                tabSelected = tabSelected,
                onTabSelected = { tabSelected = it }
            )
        },
        frontLayerContent = {
            when (tabSelected) {
                CraneScreen.Fly -> {
                    ExploreSection(
                        title = stringResource(R.string.explore_fly),
                        exploreList = suggestedDestinations,
                        onItemClicked = onExploreItemClicked
                    )
                }

                CraneScreen.Sleep -> {
                    ExploreSection(
                        title = stringResource(R.string.explore_sleep),
                        exploreList = viewModel.hotels,
                        onItemClicked = onExploreItemClicked
                    )
                }

                CraneScreen.Eat -> {
                    ExploreSection(
                        title = stringResource(R.string.explore_restaurants),
                        exploreList = viewModel.restaurants,
                        onItemClicked = onExploreItemClicked
                    )
                }
            }
        },
        backLayerContent = {
            SearchContent(
                tabSelected = tabSelected,
                viewModel = viewModel,
                onPeopleChanged = onPeopleChanged
            )
        }
    )
}

@Composable
fun HomeTabBar(
    openDrawer: () -> Unit,
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    CraneTabBar(
        modifier = modifier,
        onMenuClicked = openDrawer
    ) { tabBarModifier ->
        CraneTabs(
            modifier = tabBarModifier,
            titles = CraneScreen.entries.map { it.name },
            tabSelected = tabSelected,
            onTabSelected = { newTab ->
                onTabSelected.invoke(CraneScreen.entries[newTab.ordinal])
            }
        )
    }
}

@Composable
private fun SearchContent(
    tabSelected: CraneScreen,
    viewModel: MainViewModel,
    onPeopleChanged: (Int) -> Unit
) {
    when (tabSelected) {
        CraneScreen.Fly -> FlySearchContent(
            onPeopleChanged = onPeopleChanged,
            onToDestinationChanged = viewModel::toDestinationsChanged
        )

        CraneScreen.Sleep -> SleepSearchContent(
            onPeopleChanged = onPeopleChanged
        )

        CraneScreen.Eat -> EatSearchContent(
            onPeopleChanged = onPeopleChanged
        )
    }
}