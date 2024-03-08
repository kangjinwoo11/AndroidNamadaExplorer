package jnkadks.namada.namadaexplorer.uis.composable.pages.validator_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.models.BlockSignature
import jnkadks.namada.namadaexplorer.networks.StakepoolNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValidatorDetailsViewModel @Inject constructor(
    private val network: StakepoolNetwork
) : ViewModel() {
    var blockSignaturesState by mutableStateOf<DataState<List<BlockSignature>>>(
        DataState.Loading()
    )
        private set

    fun getBlockSignature(address: String) {
        blockSignaturesState = DataState.Loading()
        viewModelScope.launch {
            blockSignaturesState = try {
                val response = network.fetchBlockSignatures(address = address)
                DataState.Success(response)
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}