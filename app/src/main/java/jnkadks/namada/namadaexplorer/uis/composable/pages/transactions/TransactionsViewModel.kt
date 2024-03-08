package jnkadks.namada.namadaexplorer.uis.composable.pages.transactions

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.common.paging.IDataPagingListener
import jnkadks.namada.namadaexplorer.common.paging.rpcPager
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.models.PagerModel
import jnkadks.namada.namadaexplorer.models.Transaction
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val network: RpcNetwork
) : ViewModel() {
    val mapBlock: MutableMap<String, Block> = mutableMapOf()

    private val _pagingData = rpcPager(
        callback = object : IDataPagingListener<Transaction, PagerModel<Transaction>> {
            override suspend fun loadData(page: Int): PagerModel<Transaction> {
                val transactions =
                    network.fetchTransactions(page = page, pageSize = Common.limitPage)
                for (transaction in transactions.data) {
                    val elementBlock = mapBlock[transaction.blockID]
                    if(elementBlock == null) {
                        mapBlock[transaction.blockID] = network.fetchBlock(id = transaction.blockID)
                    }
                }
                return transactions
            }
        }
    )

    val pagingData: Flow<PagingData<Transaction>>
        get() {
            return _pagingData.flow
        }
}