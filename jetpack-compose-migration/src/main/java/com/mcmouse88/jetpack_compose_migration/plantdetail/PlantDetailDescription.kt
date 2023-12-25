package com.mcmouse88.jetpack_compose_migration.plantdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    Surface {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_normal))
        ) {
            PlantName(name = plant.name)
            PlantWatering(wateringInterval = plant.wateringInterval)
        }
    }
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

@Composable
private fun PlantWatering(wateringInterval: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Same modifier used by both Texts
        val centeredWithPaddingModifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)

        val normalPadding = dimensionResource(id = R.dimen.margin_normal)
        
        Text(
            text = stringResource(id = R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = centeredWithPaddingModifier.padding(top = normalPadding)
        )

        val wateringIntervalText = pluralStringResource(
            R.plurals.watering_needs_suffix,
            wateringInterval,
            wateringInterval
        )

        Text(
            text = wateringIntervalText,
            modifier = centeredWithPaddingModifier.padding(bottom = normalPadding)
        )
    }
}

@Preview
@Composable
private fun PlantWateringPreview() {
    MaterialTheme {
        PlantWatering(wateringInterval = 7)
    }
}