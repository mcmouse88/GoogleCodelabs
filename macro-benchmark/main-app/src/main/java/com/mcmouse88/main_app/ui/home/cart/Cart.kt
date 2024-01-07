package com.mcmouse88.main_app.ui.home.cart

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mcmouse88.main_app.R
import com.mcmouse88.main_app.models.OrderLine
import com.mcmouse88.main_app.models.SnackCollection
import com.mcmouse88.main_app.models.SnackRepo
import com.mcmouse88.main_app.ui.components.BenchMarkButton
import com.mcmouse88.main_app.ui.components.BenchMarkDivider
import com.mcmouse88.main_app.ui.components.BenchMarkSurface
import com.mcmouse88.main_app.ui.components.QuantitySector
import com.mcmouse88.main_app.ui.components.SnackCollection
import com.mcmouse88.main_app.ui.components.SnackImage
import com.mcmouse88.main_app.ui.home.DestinationBar
import com.mcmouse88.main_app.ui.theme.AlphaNearOpaque
import com.mcmouse88.main_app.ui.theme.BenchMarkTheme
import com.mcmouse88.main_app.ui.utils.formatPrice

@Composable
fun Cart(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel(factory = CartViewModel.provideFactory())
) {
    val orderLines by viewModel.orderLines.collectAsState()
    val inspiredByCart = remember(SnackRepo::getInspiredByCart)

    Cart(
        orderLines = orderLines,
        removeSnack = viewModel::removeSnack,
        increaseItemCount = viewModel::increaseSnackCount,
        decreaseItemCount = viewModel::decreaseSnackCount,
        inspiredByCart = inspiredByCart,
        onSnackClick = onSnackClick,
        modifier = modifier
    )
}

@Composable
fun Cart(
    orderLines: List<OrderLine>,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    BenchMarkSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            CartContent(
                orderLines = orderLines,
                removeSnack = removeSnack,
                increaseItemCount = increaseItemCount,
                decreaseItemCount = decreaseItemCount,
                inspiredByCart = inspiredByCart,
                onSnackClick = onSnackClick,
                modifier = modifier
            )
            DestinationBar(modifier = Modifier.align(Alignment.TopCenter))
            CheckoutBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun CartContent(
    orderLines: List<OrderLine>,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    inspiredByCart: SnackCollection,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val snackCountFormattedString = remember(orderLines.size, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size,
            orderLines.size
        )
    }

    LazyColumn(modifier = modifier) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
            Text(
                text = stringResource(id = R.string.cart_order_header, snackCountFormattedString),
                style = MaterialTheme.typography.h6,
                color = BenchMarkTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }

        items(orderLines) { orderLine ->
            SwipeDismissItem(
                background = { offsetX ->
                    // Background color changes from light gray to red when the swipe
                    // to delete with exceed 160.dp
                    val backgroundColor = if (offsetX < (-160).dp) {
                        BenchMarkTheme.colors.error
                    } else {
                        BenchMarkTheme.colors.uiFloated
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(backgroundColor),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ){
                        // Set 4.dp padding only if offset is bigger than 160.dp
                        val padding: Dp by animateDpAsState(
                            targetValue = if (offsetX > (-160).dp) 4.dp else 0.dp,
                            label = ""
                        )
                        Box(
                            modifier = Modifier
                                .width(offsetX * -1)
                                .padding(padding)
                        ) {
                            // Height equals to width removing padding
                            val height = (offsetX + 8.dp) * -1
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                color = BenchMarkTheme.colors.error
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Icon must be visible while in this width range
                                    if (offsetX < (-40).dp && offsetX > (-152).dp) {
                                        val iconAlpha: Float by animateFloatAsState(
                                            targetValue = if (offsetX < -120.dp) 0.5f else 1f,
                                            label = ""
                                        )

                                        Icon(
                                            imageVector = Icons.Filled.DeleteForever,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .graphicsLayer(alpha = iconAlpha),
                                            tint = BenchMarkTheme.colors.uiBackground
                                        )
                                    }

                                    // Text opacity increases as the text is supposed to apear
                                    // in the screen
                                    val textAlpha by animateFloatAsState(
                                        targetValue = if (offsetX > (-144).dp) 0.5f else 1f,
                                        label = ""
                                    )
                                    if (offsetX < (-120).dp) {
                                        Text(
                                            text = stringResource(id = R.string.remove_item),
                                            style = MaterialTheme.typography.subtitle1,
                                            color = BenchMarkTheme.colors.uiBackground,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .graphicsLayer(alpha = textAlpha)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            ) {
                CartItem(
                    orderLine = orderLine,
                    removeSnack = removeSnack,
                    increaseItemCount = increaseItemCount,
                    decreaseItemCount = decreaseItemCount,
                    onSnackClick = onSnackClick
                )
            }
        }
        item {
            SummaryItem(
                subtotal = orderLines.sumOf { it.snack.price * it.count },
                shippingCosts = 369
            )
        }
        item {
            SnackCollection(
                snackCollection = inspiredByCart,
                onSnackClick = onSnackClick,
                highlights = false
            )
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
fun CartItem(
    orderLine: OrderLine,
    removeSnack: (Long) -> Unit,
    increaseItemCount: (Long) -> Unit,
    decreaseItemCount: (Long) -> Unit,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snack = orderLine.snack
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSnackClick.invoke(snack.id) }
            .background(BenchMarkTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)

        SnackImage(
            imageUrl = snack.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = snack.name,
            style = MaterialTheme.typography.subtitle1,
            color = BenchMarkTheme.colors.textSecondary,
            modifier = Modifier
                .constrainAs(name) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = remove.start,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )

        IconButton(
            onClick = { removeSnack.invoke(snack.id) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(id = R.string.label_remove),
                tint = BenchMarkTheme.colors.iconSecondary
            )
        }

        Text(
            text = snack.tagline,
            style = MaterialTheme.typography.body1,
            color = BenchMarkTheme.colors.textHelp,
            modifier = Modifier
                .constrainAs(tag) {
                    linkTo(
                        start = image.end,
                        startMargin = 16.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )

        Text(
            text = formatPrice(snack.price),
            style = MaterialTheme.typography.subtitle1,
            color = BenchMarkTheme.colors.textPrimary,
            modifier = Modifier
                .constrainAs(price) {
                    linkTo(
                        start = image.end,
                        end = quantity.start,
                        startMargin = 16.dp,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )

        QuantitySector(
            count = orderLine.count,
            decreaseItemCount = { decreaseItemCount.invoke(snack.id) },
            increaseItemCount = { increaseItemCount.invoke(snack.id) },
            modifier = Modifier
                .constrainAs(quantity) {
                    baseline.linkTo(price.baseline)
                    end.linkTo(parent.end)
                }
        )

        BenchMarkDivider(
            modifier = Modifier
                .constrainAs(divider) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
fun SummaryItem(
    subtotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.cart_summary_header),
            style = MaterialTheme.typography.h6,
            color = BenchMarkTheme.colors.brand,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )

        Row(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cart_subtotal_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )

            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cart_shipping_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )

            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        BenchMarkDivider()
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cart_total_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .wrapContentWidth(Alignment.End)
                    .alignBy(LastBaseline)
            )

            Text(
                text = formatPrice(subtotal + shippingCosts),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }

        BenchMarkDivider()
    }
}

@Composable
private fun CheckoutBar(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(BenchMarkTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque))
    ) {
        BenchMarkDivider()
        Row {
            Spacer(modifier = Modifier.weight(1f))
            BenchMarkButton(
                onClick = { /*TODO*/ },
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.cart_checkout),
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(name = "default")
@Preview(name = "dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "large font", fontScale = 2f)
@Composable
fun CartPreview() {
    BenchMarkTheme {
        Cart(
            orderLines = SnackRepo.getCart(),
            removeSnack = {},
            increaseItemCount = {},
            decreaseItemCount = {},
            inspiredByCart = SnackRepo.getInspiredByCart(),
            onSnackClick = {}
        )
    }
}