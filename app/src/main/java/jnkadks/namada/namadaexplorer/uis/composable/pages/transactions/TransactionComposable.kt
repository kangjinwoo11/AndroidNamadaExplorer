package jnkadks.namada.namadaexplorer.uis.composable.pages.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.models.Transaction
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.FailedComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.MiddleText
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.PagingComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.components.SuccessComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details.TransactionParameter
import java.util.Date

@Composable
fun TransactionsComposable(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val now = Date()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationComposable(
        navController = navController,
        title = "Transactions"
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
                TransactionComposable(
                    now = now,
                    blocks = viewModel.mapBlock,
                    item = item,
                    modifier = Modifier.clickable {
                        val block = viewModel.mapBlock[item.blockID]
                        if (block != null) {
                            navBackStackEntry?.savedStateHandle?.set(
                                "transaction",
                                TransactionParameter.TransactionObject(
                                    item,
                                    block
                                )
                            )
                            navController.navigate("transaction_details")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TransactionComposable(
    now: Date,
    blocks: Map<String, Block>,
    item: Transaction,
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
                Row {
                    Text(
                        text = item.txType.value,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                MiddleText(
                    text = item.hash.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (item.txType == Transaction.TxType.Wrapper) {
                        SuccessComposable()
                    } else if (item.returnCode == 0L) {
                        SuccessComposable()
                    } else {
                        FailedComposable()
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    val block = blocks[item.blockID]
                    if (block != null) {
                        val date = Common.stringToDate(block.header.time)
                        val stringDate: String = if (date == null) {
                            block.header.time
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
}