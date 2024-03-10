package jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.models.Transaction
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.FailedComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.components.SuccessComposable
import jnkadks.namada.namadaexplorer.uis.composable.hook.OnStarted
import jnkadks.namada.namadaexplorer.uis.composable.pages.block_details.BlockParameter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
sealed interface TransactionParameter : Parcelable {
    @Parcelize
    data class TransactionHash(val hash: String) : TransactionParameter

    @Parcelize
    data class TransactionObject(val transaction: Transaction, val block: Block) :
        TransactionParameter
}

@Composable
fun TransactionDetailsComposable(
    navController: NavController,
    parameter: TransactionParameter,
    viewModel: TransactionDetailsViewModel = hiltViewModel()
) {
    NavigationComposable(
        navController = navController,
        title = "Information"
    ) {
        when (parameter) {
            is TransactionParameter.TransactionHash -> {
                val lifecycleOwner = LocalLifecycleOwner.current
                val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
                OnStarted(lifecycleState = lifecycleState) {
                    viewModel.loadBlock(hash = parameter.hash)
                }

                DataStateComposable(
                    dataState = viewModel.transactionState,
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
                ) { data ->
                    TransactionDetailsComposable(
                        navController = navController,
                        transaction = data.transaction,
                        block = data.block
                    )
                }
            }

            is TransactionParameter.TransactionObject -> {
                TransactionDetailsComposable(
                    navController = navController,
                    transaction = parameter.transaction,
                    block = parameter.block
                )
            }
        }
    }
}

@Composable
private fun TransactionDetailsComposable(
    navController: NavController,
    transaction: Transaction,
    block: Block
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Box(modifier = Modifier.padding(all = 16.dp)) {
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
                Text(text = "TxHash:", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = transaction.hash.uppercase(),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Status:", style = MaterialTheme.typography.bodySmall)
                if (transaction.txType == Transaction.TxType.Wrapper) {
                    SuccessComposable()
                } else if (transaction.returnCode == 0L) {
                    SuccessComposable()
                } else {
                    FailedComposable()
                }

                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {
                        navBackStackEntry?.savedStateHandle?.set(
                            "block",
                            BlockParameter.BlockObject(block)
                        )
                        navController.navigate("block_details")
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Text(text = "Height:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = block.header.height,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val now = Date()

                val date = Common.stringToDate(block.header.time)
                val stringDate: String = if (date == null) {
                    block.header.time
                } else {
                    Common.timeAgoString(now, date)
                }
                Text(text = "Time:", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "$stringDate (${Common.dateToString(date!!)})",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Fee:", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "${transaction.feeAmountPerGasUnit ?: "0"} NAAN",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Shielded:", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if (transaction.tx?.transfer?.shielded != null) {
                        "Yes"
                    } else {
                        "No"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}