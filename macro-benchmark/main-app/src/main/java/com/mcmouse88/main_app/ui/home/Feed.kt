package com.mcmouse88.main_app.ui.home

import android.content.res.Configuration
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import com.mcmouse88.main_app.models.Filter
import com.mcmouse88.main_app.models.SnackCollection
import com.mcmouse88.main_app.models.SnackRepo
import com.mcmouse88.main_app.ui.components.BenchMarkDivider
import com.mcmouse88.main_app.ui.components.BenchMarkSurface
import com.mcmouse88.main_app.ui.components.FilterBar
import com.mcmouse88.main_app.ui.components.SnackCollection
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme
import kotlinx.coroutines.delay

@Composable
fun Feed(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Simulate loading data asynchronously.
    // In real world application, you shouldn't have this kind of logic in your UI code,
    // but you should move it to appropriate layer.
    var snackCollections by remember {
        mutableStateOf<List<SnackCollection>>(listOf())
    }
    LaunchedEffect(
        key1 = Unit,
        block = {
            trace("Snack loading") {
                delay(300)
                snackCollections = SnackRepo.getSnacks()
            }
        }
    )

    val filters = remember { SnackRepo.getFilters() }
    Feed(
        snackCollections = snackCollections,
        filters = filters,
        onSnackClick = onSnackClick,
        modifier = modifier
    )
}

@Composable
private fun Feed(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    BenchMarkSurface(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            SnackCollectionList(snackCollections, filters, onSnackClick)
            DestinationBar()
        }
    }
}

@Composable
private fun SnackCollectionList(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        LazyColumn(modifier = Modifier.testTag("snack_list")) {
            item {
                Spacer(
                    modifier = Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                    )
                )
                FilterBar(
                    filters = filters,
                    onShowFilters = { filtersVisible = true }
                )
            }

            if (snackCollections.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight(0.75f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BenchMarkTheme.colors.brand)
                    }
                }
            } else {
                itemsIndexed(snackCollections) { index, item ->
                    if (index > 0) {
                        BenchMarkDivider(thickness = 2.dp)
                    }

                    SnackCollection(
                        snackCollection = item,
                        onSnackClick = onSnackClick,
                        index = index,
                        modifier = Modifier.testTag("snack_collection")
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = filtersVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        FilterScreen(
            onDismiss = { filtersVisible = false }
        )
    }

    // Reports when the UI usable by user
    ReportDrawnWhen { snackCollections.isNotEmpty() }
}

@Preview(name = "default")
@Preview(name = "dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "large font", fontScale = 2f)
@Composable
fun HomePreview() {
    BenchMarkTheme {
        Feed(
            onSnackClick = {},
            snackCollections = SnackRepo.getSnacks(),
            filters = SnackRepo.getFilters()
        )
    }
}