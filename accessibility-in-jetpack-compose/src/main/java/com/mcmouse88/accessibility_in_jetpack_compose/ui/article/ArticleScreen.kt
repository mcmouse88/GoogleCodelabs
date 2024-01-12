package com.mcmouse88.accessibility_in_jetpack_compose.ui.article

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.accessibility_in_jetpack_compose.R
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.PostsRepository
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.post3
import com.mcmouse88.accessibility_in_jetpack_compose.model.Post
import com.mcmouse88.accessibility_in_jetpack_compose.ui.components.InsetAwareTopAppBar
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme
import com.mcmouse88.accessibility_in_jetpack_compose.utils.supportWideScreen

/**
 * Stateful Article Screen that manages state using [produceUiState]
 *
 * @param postId (state) the post to show
 * @param postsRepository data source for this screen
 * @param onBack (event) request back navigation
 */
@Composable
fun ArticleScreen(
    postId: String?,
    postsRepository: PostsRepository,
    onBack: () -> Unit
) {
    val postData = postsRepository.getPost(postId)

    postData?.let {
        ArticleScreen(
            post = postData,
            onBack = onBack
        )
    }
}

/**
 * Stateless Article Screen that displays a single post.
 *
 * @param post (state) item to display
 * @param onBack (event) request navigate back
 */
@Composable
fun ArticleScreen(
    post: Post,
    onBack: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (showDialog) {
        FunctionalityNotAvailablePopup { showDialog = false }
    }

    Scaffold(
        topBar = {
            InsetAwareTopAppBar(
                title = {
                    Text(
                        text = "Published in: ${post.publication?.name}",
                        style = MaterialTheme.typography.subtitle2,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_navigate_up)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PostContent(
            post = post,
            modifier = Modifier
                // innerPadding takes into account the top and bottom
                .padding(innerPadding)
                // center content in the landscape mode
                .supportWideScreen()
        )
    }
}

/**
 * Display a popup explaining functionality not available.
 *
 * @param onDismiss (event) request the popup be dismissed
 */
@Composable
fun FunctionalityNotAvailablePopup(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
            }
        }
    )
}

@Preview("Article screen")
@Preview("Article screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Article screen (big font)", fontScale = 1.5f)
@Preview("Article screen (large screen)", device = Devices.PIXEL_C)
@Composable
fun PreviewArticle() {
    AccessibilityInJetpackComposeTheme {
        ArticleScreen(
            post = PostsRepository().getPost(post3.id)!!,
            onBack = {}
        )
    }
}