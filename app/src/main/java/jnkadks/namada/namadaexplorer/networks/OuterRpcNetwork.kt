package jnkadks.namada.namadaexplorer.networks

import jnkadks.namada.namadaexplorer.models.ListValidators
import retrofit2.http.GET

interface OuterRpcNetwork {
    @GET("validators/list")
    suspend fun fetchValidators(): ListValidators
}