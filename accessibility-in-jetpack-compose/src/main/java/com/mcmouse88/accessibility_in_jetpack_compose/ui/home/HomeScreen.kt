package com.mcmouse88.accessibility_in_jetpack_compose.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mcmouse88.accessibility_in_jetpack_compose.R
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.PostsRepository
import com.mcmouse88.accessibility_in_jetpack_compose.model.Post
import com.mcmouse88.accessibility_in_jetpack_compose.ui.components.InsetAwareTopAppBar
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Stateful HomeScreen which manages state using [produceUiState]
 *
 * @param postsRepository data source for this screen
 * @param navigateToArticle (event) request navigation to Article screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for the [Scaffold] component on this screen
 */
@Composable
fun HomeScreen(
    postsRepository: PostsRepository,
    navigateToArticle: (String) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    HomeScreen(
        posts = postsRepository.getPosts(),
        navigateToArticle = navigateToArticle,
        openDrawer = openDrawer,
        scaffoldState = scaffoldState
    )
}

/**
 * Responsible for displaying the Home Screen of this application.
 *
 * Stateless composable is not coupled to any specific state management.
 *
 * @param posts (state) the data to show on the screen
 * @param navigateToArticle (event) request navigation to Article screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for the [Scaffold] component on this screen
 */
@Composable
fun HomeScreen(
    posts: List<Post>,
    navigateToArticle: (String) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            val title = stringResource(id = R.string.jet_news)
            InsetAwareTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(
                        onClick = { scope.launch { openDrawer.invoke() } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_jet_news_logo),
                            contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        PostList(
            posts = posts,
            navigateToArticle = navigateToArticle,
            modifier = modifier
        )
    }
}

/**
 * Display a list of posts.
 *
 * When a post is clicked on, [navigateToArticle] will be called to navigate to the detail screen
 * for that post.
 *
 * @param posts (state) the list to display
 * @param navigateToArticle (event) request navigation to Article screen
 * @param modifier modifier for the root element
 */
@Composable
private fun PostList(
    posts: List<Post>,
    navigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val postsHistory = posts.subList(0, 3)
    val postsPopular = posts.subList(3, 5)
    val contentPadding = rememberContentPaddingForScreen(additionalTop = 8.dp)

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(postsHistory) { post ->
            PostCardHistory(
                post = post,
                navigateToArticle = navigateToArticle
            )
            PostListDivider()
        }
        item {
            PostListPopularSection(
                posts = postsPopular,
                navigateToArticle = navigateToArticle
            )
        }
    }
}

/**
 * Horizontal scrolling cards for [PostList]
 *
 * @param posts (state) to display
 * @param navigateToArticle (event) request navigation to Article screen
 */
@Composable
private fun PostListPopularSection(
    posts: List<Post>,
    navigateToArticle: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.home_popular_section_title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(posts) { post ->
                PostCardPopular(
                    post = post,
                    navigateToArticle = navigateToArticle,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }

        PostListDivider()
    }
}

/**
 * Full-width divider with padding for [PostList]
 */
@Composable
private fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}

/**
 * Determine the content padding to apply to the different screens of the app
 */
@Composable
fun rememberContentPaddingForScreen(additionalTop: Dp = 0.dp): PaddingValues {
    return WindowInsets.systemBars
        .only(WindowInsetsSides.Bottom)
        .add(WindowInsets(top = additionalTop))
        .asPaddingValues()
}

@Preview("Home screen")
@Preview("Home screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Home screen (big font)", fontScale = 1.5f)
@Preview("Home screen (large screen)", device = Devices.PIXEL_C)
@Composable
fun PreviewHomeScreen() {
    AccessibilityInJetpackComposeTheme {
        HomeScreen(
            posts = PostsRepository().getPosts(),
            navigateToArticle = {},
            openDrawer = {},
            scaffoldState = rememberScaffoldState()
        )
    }
}