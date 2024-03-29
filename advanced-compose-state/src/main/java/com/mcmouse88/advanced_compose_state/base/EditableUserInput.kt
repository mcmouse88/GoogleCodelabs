package com.mcmouse88.advanced_compose_state.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import com.mcmouse88.advanced_compose_state.ui.theme.captionTextStyle

class EditableUserInputState(
    private val hint: String,
    initialText: String
) {

    var text by mutableStateOf(initialText)
        private set

    fun updateText(newText: String) {
        text = newText
    }

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<EditableUserInputState, *> = listSaver(
            save = { listOf(it.hint, it.text) },
            restore = { listSaver ->
                EditableUserInputState(
                    hint = listSaver[0],
                    initialText = listSaver[1]
                )
            }
        )
    }
}

@Composable
fun rememberEditableUserInputState(hint: String): EditableUserInputState {
    return rememberSaveable(hint, saver = EditableUserInputState.Saver) {
        EditableUserInputState(hint, hint)
    }
}

@Composable
fun CraneEditableUserInput(
    state: EditableUserInputState = rememberEditableUserInputState(hint = ""),
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null
) {
    CraneBaseUserInput(
        caption = caption,
        showCaption = { state.isHint.not() },
        vectorImageId = vectorImageId,
        tintIcon = { state.isHint.not() }
    ) {
        BasicTextField(
            value = state.text,
            onValueChange = { state.updateText(it) },
            textStyle = if (state.isHint) {
                captionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}