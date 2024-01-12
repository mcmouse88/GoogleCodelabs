package com.mcmouse88.accessibility_in_jetpack_compose.ui.interests

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mcmouse88.accessibility_in_jetpack_compose.R
import com.mcmouse88.accessibility_in_jetpack_compose.data.interests.InterestsRepository
import com.mcmouse88.accessibility_in_jetpack_compose.data.interests.TopicSelection
import com.mcmouse88.accessibility_in_jetpack_compose.data.interests.TopicsMap
import com.mcmouse88.accessibility_in_jetpack_compose.ui.components.InsetAwareTopAppBar
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme
import kotlinx.coroutines.launch

/**
 * Stateful InterestsScreen that handles the interaction with the repository
 *
 * @param interestsRepository data source for this screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for screen Scaffold
 */
@Composable
fun InterestsScreen(
    interestsRepository: InterestsRepository,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    // Returns a [CoroutineScope] that is scoped to the lifecycle of [InterestsScreen]. When this
    // screen is removed from composition, the scope will be cancelled.
    val scope = rememberCoroutineScope()

    // collectAsState will read a Flow in Compose
    val selectedTopics by interestsRepository.observeTopicsSelected().collectAsState(emptySet())
    val onTopicSelect: (TopicSelection) -> Unit = {
        scope.launch { interestsRepository.toggleTopicSelection(it) }
    }

    InterestsScreen(
        topics = interestsRepository.topics,
        selectedTopics = selectedTopics,
        onTopicSelect = onTopicSelect,
        openDrawer = openDrawer,
        scaffoldState = scaffoldState,
        modifier = modifier
    )
}

/**
 * Stateless interest screen displays the topics the user can subscribe to
 *
 * @param topics (state) topics to display, mapped by section
 * @param selectedTopics (state) currently selected topics
 * @param onTopicSelect (event) request a topic selection be changed
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) the state for the screen's [Scaffold]
 */
@Composable
fun InterestsScreen(
    topics: TopicsMap,
    selectedTopics: Set<TopicSelection>,
    onTopicSelect: (TopicSelection) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InsetAwareTopAppBar(
                title = { Text(text = "Interests") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_jet_news_logo),
                            contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                        )
                    }
                }
                )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.padding(padding)
        ) {
            topics.forEach { (section, topics) ->
                item {
                    Text(
                        text = section,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(topics) { topic ->
                    TopicItem(
                        itemTitle = topic,
                        selected = selectedTopics.contains(TopicSelection(section, topic))
                    ) {
                        onTopicSelect(TopicSelection(section, topic))
                    }
                    TopicDivider()
                }
            }
        }
    }
}

/**
 * Display a full-width topic item
 *
 * @param itemTitle (state) topic title
 * @param selected (state) is topic currently selected
 * @param onToggle (event) toggle selection for topic
 */
@Composable
private fun TopicItem(
    itemTitle: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val image = painterResource(id = R.drawable.placeholder_1_1)
    Row(
        modifier = Modifier
            .toggleable(
                value = selected,
                onValueChange = { onToggle.invoke() },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(width = 56.dp, height = 56.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Text(
            text = itemTitle,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = selected,
            onCheckedChange = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

/**
 * Full-width divider for topics
 */
@Composable
private fun TopicDivider() {
    Divider(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
        modifier = Modifier.padding(start = 90.dp)
    )
}

@Preview("Interests screen", "Interests")
@Preview("Interests screen (dark)", "Interests", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Interests screen (big font)", "Interests", fontScale = 1.5f)
@Preview("Interests screen (large screen)", "Interests", device = Devices.PIXEL_C)
@Composable
fun PreviewInterestsScreen() {
    AccessibilityInJetpackComposeTheme {
        InterestsScreen(
            interestsRepository = InterestsRepository(),
            openDrawer = {}
        )
    }
}