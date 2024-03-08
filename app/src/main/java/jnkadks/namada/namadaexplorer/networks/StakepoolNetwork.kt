package jnkadks.namada.namadaexplorer.networks

import jnkadks.namada.namadaexplorer.models.BlockSignature
import retrofit2.http.GET
import retrofit2.http.Path

interface StakepoolNetwork {
    @GET("node/validators/validator/{address}/latestSignatures")
    suspend fun fetchBlockSignatures(@Path("address") address: String): List<BlockSignature>
}