package jnkadks.namada.namadaexplorer.uis.composable.pages.block_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.networks.RpcNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockDetailsViewModel @Inject constructor(
    private val rpcNetwork: RpcNetwork
) : ViewModel() {
    var blockState by mutableStateOf<DataState<Block>>(
        DataState.Loading()
    )
        private set

    fun loadBlock(hash: String) {
        blockState = DataState.Loading()
        viewModelScope.launch {
            blockState = try {
                val response = rpcNetwork.fetchBlock(id = hash)
                DataState.Success(response)
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}