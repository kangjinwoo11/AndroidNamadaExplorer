package jnkadks.namada.namadaexplorer.uis.composable.pages.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.networks.OuterRpcNetwork
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import jnkadks.namada.namadaexplorer.uis.composable.pages.home.data.HomeData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rpcNetwork: RpcNetwork,
    private val outerRpcNetwork: OuterRpcNetwork
) : ViewModel() {
    var homeState by mutableStateOf<DataState<HomeData>>(
        DataState.Loading()
    )
        private set

    init {
        loadValidators()
    }

    fun loadValidators() {
        homeState = DataState.Loading()
        viewModelScope.launch {
            homeState = try {
                val blocks = rpcNetwork.fetchBlocks(1, 1)
                val blockSize = blocks.total
                val latestBlock = blocks.data.first()
                val validators = outerRpcNetwork.fetchValidators()
                val homeData = HomeData(
                    blocksSize = blockSize,
                    latestBlockDate = Common.stringToDate(latestBlock.header.time),
                    network = latestBlock.header.chainID,
                    validatorsSize = validators.currentValidatorsList.size
                )
                DataState.Success(homeData)
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}