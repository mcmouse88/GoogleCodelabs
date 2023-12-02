package com.mcmouse88.theming_compose.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.theming_compose.data.LocalEmailsDataProvider
import com.mcmouse88.theming_compose.ui.theme.GoogleCodelabTheme

// https://developer.android.com/codelabs/jetpack-compose-theming#0

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ReplyHomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            GoogleCodelabTheme {
                ReplyApp(
                    replyHomeUIState = uiState,
                    closeDetailScreen = viewModel::closeDetailScreen,
                    navigateToDetail = viewModel::setSelectedEmail
                )
            }
        }
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    name = "Night"
)
@Preview(
    uiMode = UI_MODE_NIGHT_NO,
    name = "Light"
)
@Composable
fun GreetingPreview() {
    GoogleCodelabTheme {
        ReplyApp(
            replyHomeUIState = ReplyHomeUIState(
                emails = LocalEmailsDataProvider.allEmails
            )
        )
    }
}