package com.mcmouse88.jetpack_compose_migration.plantdetail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.mcmouse88.jetpack_compose_migration.R
import com.mcmouse88.jetpack_compose_migration.data.Plant
import com.mcmouse88.jetpack_compose_migration.viewmodels.PlantDetailViewModel

@Composable
fun PlantDetailDescription(
    viewModel: PlantDetailViewModel
) {
    // Observes values coming from the VM's LiveData<Plant> field
    val plant by viewModel.plant.observeAsState()
    // If plant is not null, display the content
    plant?.let {
        PlantDetailContent(it)
    }
}

@Composable
fun PlantDetailContent(plant: Plant) {
    PlantName(name = plant.name)
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    val plant = Plant(
        plantId = "id",
        name = "Apple",
        description = "description",
        growZoneNumber = 3,
        wateringInterval = 30,
        imageUrl = ""
    )

    MaterialTheme {
        PlantDetailContent(plant = plant)
    }
}

@Composable
private fun PlantName(name: String) {
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