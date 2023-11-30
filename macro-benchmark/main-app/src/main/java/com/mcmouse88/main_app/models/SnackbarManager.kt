package com.mcmouse88.main_app.models

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class Message(val id: Long, @StringRes val messageId: Int)

/**
 * Class responsible for managing Snackbar messages to show on the screen
 */
object SnackbarManager {

    private val _message: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val message: StateFlow<List<Message>> get() = _message.asStateFlow()

    fun showMessage(@StringRes messageTextId: Int) {
        _message.update { currentMessage ->
            currentMessage + Message(
                id = UUID.randomUUID().mostSignificantBits,
                messageId = messageTextId
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        _message.update { currentMessage ->
            currentMessage.filterNot { it.id == messageId }
        }
    }
}