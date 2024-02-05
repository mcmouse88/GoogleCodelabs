package com.mcmouse88.android_privacy.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mcmouse88.android_privacy.data.local.entity.MAX_LOG_PHOTOS_LIMIT
import java.io.File

@Composable
fun PhotoGrid(
    photos: List<File>,
    modifier: Modifier = Modifier,
    onRemove: ((photo: File) -> Unit)? = null
) {
    Row(
        modifier = modifier
    ) {
        repeat(MAX_LOG_PHOTOS_LIMIT) { index ->
            val file = photos.getOrNull(index)

            if (file == null) {
                Box(modifier = Modifier.weight(1f))
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = file,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    if (onRemove != null) {
                        FilledTonalIconButton(onClick = { onRemove.invoke(file) }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
