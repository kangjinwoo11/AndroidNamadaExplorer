package jnkadks.namada.namadaexplorer.uis.composable.pages.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import jnkadks.namada.namadaexplorer.R
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.MiddleText
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.PagingComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.pages.block_details.BlockParameter
import java.util.Date

@Composable
fun BlocksComposable(
    navController: NavController,
    viewModel: BlocksViewModel = hiltViewModel()
) {
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val now = Date()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationComposable(
        navController = navController,
        title = "Blocks"
    ) {
        Surface {
            PagingComposable(
                pagingData = pagingData,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                onReload = {
                    pagingData.refresh()
                },
                loading = {
                    ProgressView()
                },
                empty = {

                },
                error = { e ->
                    ErrorView(error = e)
                },
            ) { item ->
                ValidatorComposable(
                    now = now,
                    item = item,
                    modifier = Modifier.clickable {
                        navBackStackEntry?.savedStateHandle?.set(
                            "block",
                            BlockParameter.BlockObject(item)
                        )
                        navController.navigate("block_details")
                    }
                )
            }
        }
    }
}

@Composable
private fun ValidatorComposable(
    now: Date,
    item: Block,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MiddleText(
                    text = item.blockID.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = Common.formattedWithCommas(item.header.height.toInt()),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "Clock",
                        modifier = Modifier.size(16.dp)
                    )
                    val date = Common.stringToDate(item.header.time)
                    val stringDate: String = if (date == null) {
                        item.header.time
                    } else {
                        Common.timeAgoString(now, date)
                    }
                    Text(
                        text = stringDate,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}