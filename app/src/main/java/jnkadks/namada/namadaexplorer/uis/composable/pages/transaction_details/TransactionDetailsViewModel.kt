package jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val rpcNetwork: RpcNetwork
) : ViewModel() {
    var transactionState by mutableStateOf<DataState<TransactionParameter.TransactionObject>>(
        DataState.Loading()
    )
        private set

    fun loadBlock(hash: String) {
        transactionState = DataState.Loading()
        viewModelScope.launch {
            transactionState = try {
                val transaction = rpcNetwork.fetchTransaction(hash = hash)
                val block = rpcNetwork.fetchBlock(transaction.blockID)
                DataState.Success(
                    TransactionParameter.TransactionObject(
                        transaction = transaction,
                        block = block
                    )
                )
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}