package jnkadks.namada.namadaexplorer.uis.composable.pages.proposals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.models.ListProposals
import jnkadks.namada.namadaexplorer.networks.NamadaRedNetwork
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProposalsViewModel @Inject constructor(
    private val redNetwork: NamadaRedNetwork
) : ViewModel() {
    var proposalsState by mutableStateOf<DataState<List<ListProposals.Proposal>>>(
        DataState.Loading()
    )
        private set

    init {
        loadProposals()
    }

    fun loadProposals() {
        proposalsState = DataState.Loading()
        viewModelScope.launch {
            proposalsState = try {
                val response = redNetwork.fetchProposals()
                DataState.Success(
                    response.proposals.sortedBy {
                        it.id
                    }
                )
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}