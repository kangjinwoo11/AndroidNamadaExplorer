package jnkadks.namada.namadaexplorer.uis.composable.pages.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jnkadks.namada.namadaexplorer.R
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVertical
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVerticalData
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.pages.home.data.HomeData
import jnkadks.namada.namadaexplorer.uis.composable.pages.home.data.HomeState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun HomeComposable(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        state = state,
        enabled = true,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        modifier = Modifier
            .fillMaxSize(),
        toolbar = {
            Card(
                Modifier
                    .height(52.dp),
                shape = RoundedCornerShape(0.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Namada explorer",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    ) {
        Surface {
            DataStateComposable(
                dataState = viewModel.homeState,
                horizontalAlignment = Alignment.CenterHorizontally,
                header = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, 8.dp),
                        modifier = Modifier
                    ) {
                        items(HomeState.entries) { item ->
                            HomeSelection(homeState = item) {
                                navController.navigate(item.des)
                            }
                        }
                    }
                },
                loading = {
                    ProgressView()
                }, error = { e ->
                    ErrorView(error = e)
                }
            ) { data ->
                HomeDataComposable(homeData = data)
            }
        }
    }
}

@Composable
private fun HomeSelection(homeState: HomeState, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = homeState.icon),
                contentDescription = homeState.title,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "${homeState.title}\n",
                maxLines = 2,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

private data class HomeDataElement(
    @DrawableRes val icon: Int,
    val color: Color,
    val title: String,
    val value: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeDataComposable(homeData: HomeData) {
    val stringDate: String = if (homeData.latestBlockDate == null) {
        ""
    } else {
        Common.dateToString(homeData.latestBlockDate)
    }
    val data = listOf(
        HomeDataElement(
            icon = R.drawable.ic_block,
            title = "Block height",
            color = Color.Cyan,
            value = Common.formattedWithCommas(homeData.blocksSize)
        ),
        HomeDataElement(
            icon = R.drawable.ic_clock,
            title = "Latest Block Time",
            color = Color.Green,
            value = stringDate
        ),
        HomeDataElement(
            icon = R.drawable.ic_network,
            title = "Network",
            color = Color.Yellow,
            value = homeData.network
        ),
        HomeDataElement(
            icon = R.drawable.ic_users,
            title = "Validators",
            color = Color.Magenta,
            value = Common.formattedWithCommas(homeData.validatorsSize)
        )
    )

    BoxWithConstraints(modifier = Modifier) {
        val boxWithConstraintsScope = this
        Box(modifier = Modifier) {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (item in data) {

                    CardVertical(
                        modifier = Modifier
                            .width((boxWithConstraintsScope.maxWidth / 2f) - 16.dp - 4.dp),
                        data = listOf(
                            CardVerticalData.Image(
                                id = item.icon,
                                backgroundColor = item.color,
                                size = 32.dp,
                                padding = 8.dp
                            ),
                            CardVerticalData.SubTitle(
                                text = item.title,
                                maxLines = 2
                            ),
                            CardVerticalData.Title(
                                text = item.value,
                                maxLines = 2
                            )
                        )
                    )
                }
            }
        }
    }
}