package com.mcmouse88.advanced_compose_state.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import com.mcmouse88.advanced_compose_state.ui.theme.captionTextStyle

@Composable
fun CraneEditableUserInput(
    hint: String,
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    onInputChanged: (String) -> Unit
) {
    // TODO: Codelab Encapsulate this state in a state holder
    var textState by remember { mutableStateOf(hint) }
    val isHint = { textState == hint }

    CraneBaseUserInput(
        caption = caption,
        showCaption = { !isHint.invoke() },
        vectorImageId = vectorImageId,
        tintIcon = { !isHint.invoke() }
    ) {
        BasicTextField(
            value = textState,
            onValueChange = {
                textState = it
                if (!isHint.invoke()) onInputChanged.invoke(textState)
            },
            textStyle = if (isHint.invoke()) {
                captionTextStyle.copy(color = LocalContentColor.current)
            } else {
                MaterialTheme.typography.body1.copy(color = LocalContentColor.current)
            },
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}