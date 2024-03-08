package jnkadks.namada.namadaexplorer.uis.composable.pages.blocks

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.common.paging.IDataPagingListener
import jnkadks.namada.namadaexplorer.common.paging.rpcPager
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.models.PagerModel
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BlocksViewModel @Inject constructor(
    private val network: RpcNetwork
) : ViewModel() {
    private val _pagingData = rpcPager(
        callback = object : IDataPagingListener<Block, PagerModel<Block>> {
            override suspend fun loadData(page: Int): PagerModel<Block> {
                return network.fetchBlocks(page = page, pageSize = Common.limitPage)
            }
        }
    )

    val pagingData: Flow<PagingData<Block>>
        get() {
            return _pagingData.flow
        }
}