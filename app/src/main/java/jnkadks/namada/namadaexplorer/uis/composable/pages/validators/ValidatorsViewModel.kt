package jnkadks.namada.namadaexplorer.uis.composable.pages.validators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.models.ListValidators
import jnkadks.namada.namadaexplorer.networks.OuterRpcNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValidatorsViewModel @Inject constructor(
    private val outerRpcNetwork: OuterRpcNetwork
) : ViewModel() {
    var validatorsState by mutableStateOf<DataState<List<ListValidators.CurrentValidator>>>(
        DataState.Loading()
    )
        private set

    init {
        loadValidators()
    }

    fun loadValidators() {
        validatorsState = DataState.Loading()
        viewModelScope.launch {
            validatorsState = try {
                val response = outerRpcNetwork.fetchValidators()
                DataState.Success(response.currentValidatorsList)
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}