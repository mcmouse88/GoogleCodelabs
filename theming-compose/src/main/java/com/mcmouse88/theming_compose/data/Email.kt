package com.mcmouse88.theming_compose.data

import androidx.annotation.DrawableRes

/**
 * A simple data class to represent an Email.
 */
data class Email(
    val id: Long,
    val sender: Account,
    val recipients: List<Account> = emptyList(),
    val subject: String,
    val body: String,
    val attachments: List<EmailAttachment> = emptyList(),
    var isImportant: Boolean = false,
    var isStarred: Boolean = false,
    var mailbox: MailboxType = MailboxType.INBOX,
    val createdAt: String,
    val threads: List<Email> = emptyList()
)

enum class MailboxType {
    INBOX, DRAFTS, SENT, SPAM, TRASH
}

data class EmailAttachment(
    @DrawableRes val resId: Int,
    val contentDesc: String
)