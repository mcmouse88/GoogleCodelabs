package com.mcmouse88.accessibility_in_jetpack_compose.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcmouse88.accessibility_in_jetpack_compose.R
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.post1
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.post3
import com.mcmouse88.accessibility_in_jetpack_compose.model.Post
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme

@Composable
fun PostCardHistory(
    post: Post,
    navigateToArticle: (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    val showFewerLabel = stringResource(id = R.string.cd_show_fewer)
    Row(
        modifier = Modifier
            .clickable(
                onClickLabel = stringResource(id = R.string.action_read_article)
            ) { navigateToArticle(post.id) }
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = showFewerLabel,
                        // action returns boolean to indicate success
                        action = { openDialog = true; true }
                    )
                )
            }
    ) {
        Image(
            painter = painterResource(id = post.imageThumbId),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .size(width = 40.dp, height = 40.dp)
                .clip(MaterialTheme.shapes.small)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            Text(text = post.title, style = MaterialTheme.typography.subtitle1)
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    val textStyle = MaterialTheme.typography.body2
                    Text(text = post.metadata.author.name, style = textStyle)
                    Text(text = " - ${post.metadata.readTimeMinutes} min read", style = textStyle)
                }
            }
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(
                onClick = { openDialog = true },
                modifier = Modifier.clearAndSetSemantics {  }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = showFewerLabel
                )
            }
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stories),
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.fewer_stories_content),
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { openDialog = false }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCardPopular(
    post: Post,
    navigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val readArticleLabel = stringResource(id = R.string.action_read_article)
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .size(width = 280.dp, height = 240.dp)
            .semantics { onClick(label = readArticleLabel, action = null) },
        onClick = { navigateToArticle(post.id) }
    ) {
        Column {
            Image(
                painter = painterResource(id = post.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = post.metadata.author.name,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(
                        id = R.string.home_post_min_read,
                        formatArgs = arrayOf(
                            post.metadata.date,
                            post.metadata.readTimeMinutes
                        )
                    ),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPostCardPopular() {
    AccessibilityInJetpackComposeTheme {
        Surface {
            PostCardPopular(
                post = post1,
                navigateToArticle = {}
            )
        }
    }
}

@Preview("Post History card")
@Composable
fun HistoryPostPreview() {
    AccessibilityInJetpackComposeTheme {
        Surface {
            PostCardHistory(
                post = post3,
                navigateToArticle = {}
            )
        }
    }
}