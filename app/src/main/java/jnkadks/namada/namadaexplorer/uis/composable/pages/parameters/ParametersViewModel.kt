package jnkadks.namada.namadaexplorer.uis.composable.pages.parameters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jnkadks.namada.namadaexplorer.local.TomlLocal
import jnkadks.namada.namadaexplorer.models.Parameters
import jnkadks.namada.namadaexplorer.uis.composable.components.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParametersViewModel @Inject constructor(
    private val tomlLocal: TomlLocal
) : ViewModel() {
    var parameterState by mutableStateOf<DataState<Parameters>>(
        DataState.Loading()
    )
        private set

    init {
        loadParameters()
    }

    fun loadParameters() {
        parameterState = DataState.Loading()
        viewModelScope.launch {
            parameterState = try {
                val response = tomlLocal.readParameters()
                DataState.Success(response)
            } catch (e: Exception) {
                DataState.Error(e)
            }
        }
    }
}