package com.mcmouse88.theming_compose.ui

import androidx.lifecycle.ViewModel
import com.mcmouse88.theming_compose.data.Email
import com.mcmouse88.theming_compose.data.LocalEmailsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReplyHomeViewModel : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(ReplyHomeUIState(loading = true))
    val uiState = _uiState.asStateFlow()

    init {
        initEmailList()
    }

    private fun initEmailList() {
        val emails = LocalEmailsDataProvider.allEmails
        _uiState.value = ReplyHomeUIState(
            emails = emails,
            selectedEmail = emails.first()
        )
    }

    /**
     * We only set isDetailOnlyOpen to true when it's only single pane layout
     */
    fun setSelectedEmail(emailId: Long) {
        val email = uiState.value.emails.find { it.id == emailId }
        _uiState.value = _uiState.value.copy(
            selectedEmail = email,
            isDetailOnlyOpen = true
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState.value.copy(
            isDetailOnlyOpen = false,
            selectedEmail = _uiState.value.emails.first()
        )
    }
}

data class ReplyHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
