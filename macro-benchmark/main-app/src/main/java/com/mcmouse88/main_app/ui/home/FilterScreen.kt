package com.mcmouse88.main_app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mcmouse88.main_app.R
import com.mcmouse88.main_app.models.Filter
import com.mcmouse88.main_app.models.SnackRepo
import com.mcmouse88.main_app.ui.components.BenchMarkScaffold
import com.mcmouse88.main_app.ui.components.FilterChip
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme

@Composable
fun FilterScreen(
    onDismiss: () -> Unit
) {
    var sortState by remember { mutableStateOf(SnackRepo.getSortDefault()) }
    var maxCalories by remember { mutableFloatStateOf(0f) }
    val defaultFilter = SnackRepo.getSortDefault()

    Dialog(onDismissRequest = onDismiss) {
        val priceFilters = remember { SnackRepo.getPriceFilters() }
        val categoryFilters = remember { SnackRepo.getCategoryFilters() }
        val lifeStyleFilters = remember { SnackRepo.getLifeStyleFilters() }
        BenchMarkScaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.label_filters),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        val resetEnabled = sortState != defaultFilter
                        IconButton(
                            onClick = { /* TODO: Open Search */ },
                            enabled = resetEnabled
                        ) {
                            val alpha = if (resetEnabled) {
                                ContentAlpha.high
                            } else {
                                ContentAlpha.disabled
                            }

                            CompositionLocalProvider(value = LocalContentAlpha provides alpha) {
                                Text(
                                    text = stringResource(id = R.string.reset),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    },
                    backgroundColor = BenchMarkTheme.colors.uiBackground
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                SortFiltersSection(
                    sortState = sortState,
                    onFilterChange = { filter ->
                        sortState = filter.name
                    }
                )

                FilterChipSection(
                    title = stringResource(id = R.string.price),
                    filters = priceFilters
                )

                FilterChipSection(
                    title = stringResource(id = R.string.category),
                    filters = categoryFilters
                )

                MaxCalories(
                    sliderPosition = maxCalories,
                    onValueChanged = { newValue ->
                        maxCalories = newValue
                    }
                )

                FilterChipSection(
                    title = stringResource(id = R.string.lifestyle),
                    filters = lifeStyleFilters
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChipSection(
    title: String,
    filters: List<Filter>
) {
    FilterTitle(text = title)
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                filter = filter,
                modifier = Modifier.padding(end = 4.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun SortFiltersSection(
    sortState: String,
    onFilterChange: (Filter) -> Unit
) {
    FilterTitle(text = stringResource(id = R.string.sort))
    Column(
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        SortFilters(
            sortState = sortState,
            onChange = onFilterChange
        )
    }
}

@Composable
fun SortFilters(
    sortState: String,
    sortFilters: List<Filter> = SnackRepo.getSortFilters(),
    onChange: (Filter) -> Unit
) {
    sortFilters.forEach { filter ->
        SortOption(
            text = filter.name,
            icon = filter.icon,
            selected = sortState == filter.name,
            onClickOption = {
                onChange.invoke(filter)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaxCalories(
    sliderPosition: Float,
    onValueChanged: (Float) -> Unit
) {
    FlowRow {
        FilterTitle(text = stringResource(id = R.string.max_calories))
        Text(
            text = stringResource(id = R.string.per_serving),
            style = MaterialTheme.typography.body2,
            color = BenchMarkTheme.colors.brand,
            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
        )
    }

    Slider(
        value = sliderPosition,
        onValueChange = { newValue ->
            onValueChanged.invoke(newValue)
        },
        valueRange = 0f..300f,
        steps = 5,
        colors = SliderDefaults.colors(
            thumbColor = BenchMarkTheme.colors.brand,
            activeTrackColor = BenchMarkTheme.colors.brand
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun FilterTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = BenchMarkTheme.colors.brand,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SortOption(
    text: String,
    icon: ImageVector?,
    onClickOption: () -> Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .selectable(selected) { onClickOption.invoke() }
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = BenchMarkTheme.colors.brand
            )
        }
    }
}

@Preview("filter_screen")
@Composable
private fun FilterScreenPreview() {
    BenchMarkTheme {
        FilterScreen(onDismiss = {})
    }
}