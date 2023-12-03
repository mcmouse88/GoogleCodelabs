package com.mcmouse88.theming_compose.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mcmouse88.theming_compose.R
import com.mcmouse88.theming_compose.data.Email
import com.mcmouse88.theming_compose.ui.components.EmailDetailAppBar
import com.mcmouse88.theming_compose.ui.components.ReplyEmailListItem
import com.mcmouse88.theming_compose.ui.components.ReplyEmailThreadItem
import com.mcmouse88.theming_compose.ui.components.ReplySearchBar

@Composable
fun ReplyInboxScreen(
    replyHomeUiState: ReplyHomeUIState,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val emailLazyListState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        ReplyEmailListContent(
            replyHomeUiState = replyHomeUiState,
            emailLazyListState = emailLazyListState,
            closeDetailScreen = closeDetailScreen,
            navigateToDetail = navigateToDetail,
            modifier = Modifier.fillMaxSize()
        )

        LargeFloatingActionButton(
            onClick = { /*TODO: Click Implementation*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.edit),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun ReplyEmailListContent(
    replyHomeUiState: ReplyHomeUIState,
    emailLazyListState: LazyListState,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (replyHomeUiState.selectedEmail != null && replyHomeUiState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen.invoke()
        }

        ReplyEmailDetail(email = replyHomeUiState.selectedEmail) {
            closeDetailScreen.invoke()
        }
    } else {
        ReplyEmailList(
            emails = replyHomeUiState.emails,
            emailLazyListState = emailLazyListState,
            modifier = modifier,
            navigateToDetail = navigateToDetail
        )
    }
}

@Composable
fun ReplyEmailList(
    emails: List<Email>,
    emailLazyListState: LazyListState,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    selectedEmail: Email? = null
) {
    LazyColumn(
        modifier = modifier,
        state = emailLazyListState
    ) {
        item {
            ReplySearchBar(modifier = Modifier.fillMaxWidth())
        }
        items(items = emails, key = { it.id }) { email ->
            ReplyEmailListItem(
                email = email,
                isSelected = email.id == selectedEmail?.id
            ) { emailId ->
                navigateToDetail.invoke(emailId)
            }
        }
    }
}

@Composable
fun ReplyEmailDetail(
    email: Email,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = true,
    onBackPressed: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        item {
            EmailDetailAppBar(
                email = email,
                isFullScreen = isFullScreen,
                onBackPressed = onBackPressed
            )
        }
        items(items = email.threads, key = { it.id }) { email ->
            ReplyEmailThreadItem(email = email)
        }
    }
}
