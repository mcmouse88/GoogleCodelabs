package com.mcmouse88.jetpack_compose_migration.plantdetail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.jetpack_compose_migration.R

@Composable
fun PlantDetailDescription() {
    Surface {
        Text("Hello Compose")
    }
}

@Composable
fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Preview
@Composable
private fun PlantNamePreview() {
    MaterialTheme {
        PlantName(name = "Apple")
    }
}