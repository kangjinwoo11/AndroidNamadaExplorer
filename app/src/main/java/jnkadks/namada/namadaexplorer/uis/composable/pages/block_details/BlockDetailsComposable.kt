package jnkadks.namada.namadaexplorer.uis.composable.pages.block_details

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import jnkadks.namada.namadaexplorer.R
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVertical
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVerticalData
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.MiddleText
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.hook.OnStarted
import jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details.TransactionParameter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
sealed interface BlockParameter : Parcelable {
    @Parcelize
    data class BlockId(val id: String) : BlockParameter

    @Parcelize
    data class BlockObject(val block: Block) : BlockParameter
}

@Composable
fun BlockDetailsComposable(
    navController: NavController,
    parameter: BlockParameter,
    viewModel: BlockDetailsViewModel = hiltViewModel()
) {
    NavigationComposable(
        navController = navController,
        title = "Header"
    ) {
        when (parameter) {
            is BlockParameter.BlockId -> {
                val lifecycleOwner = LocalLifecycleOwner.current
                val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
                OnStarted(lifecycleState = lifecycleState) {
                    viewModel.loadBlock(parameter.id)
                }

                DataStateComposable(
                    dataState = viewModel.blockState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = 52.dp, top = 16.dp)
                        .padding(bottom = 16.dp),
                    loading = {
                        ProgressView()
                    },
                    error = { e ->
                        ErrorView(error = e)
                    },
                ) { block ->
                    BlockDetailsComposable(navController = navController, block = block)
                }
            }

            is BlockParameter.BlockObject -> {
                BlockDetailsComposable(navController = navController, block = parameter.block)
            }
        }
    }
}

@Composable
private fun BlockDetailsComposable(
    navController: NavController,
    block: Block
) {
    val now = Date()

    val date = Common.stringToDate(block.header.time)
    val stringDate: String = if (date == null) {
        block.header.time
    } else {
        Common.timeAgoString(now, date)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Card(
                    modifier = Modifier,
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
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp)
                    ) {
                        Text(text = "Chain ID:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.header.chainID,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Height:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.header.height,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Block Time:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "$stringDate (${Common.dateToString(date!!)})",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Block ID:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.blockID.uppercase(),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Proposer:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.header.proposerAddress.uppercase(),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Proposer:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.txHashes.size.toString(),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CardVertical(data = listOf(CardVerticalData.Title(text = "Transactions")))
            }
        }

        items(block.txHashes) { txHash ->
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {
                        navBackStackEntry?.savedStateHandle?.set(
                            "transaction",
                            TransactionParameter.TransactionHash(txHash.hashID)
                        )
                        navController.navigate("transaction_details")
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MiddleText(
                            text = txHash.hashID.uppercase(),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = Common.formattedWithCommas(block.header.height.toInt()),
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = "Clock",
                                modifier = Modifier.size(16.dp)
                            )
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
}