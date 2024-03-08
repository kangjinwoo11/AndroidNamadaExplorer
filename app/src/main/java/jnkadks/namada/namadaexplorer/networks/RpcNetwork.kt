package jnkadks.namada.namadaexplorer.networks

import jnkadks.namada.namadaexplorer.models.Block
import jnkadks.namada.namadaexplorer.models.PagerModel
import jnkadks.namada.namadaexplorer.models.Transaction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RpcNetwork {
    @GET("block")
    suspend fun fetchBlocks(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): PagerModel<Block>

    @GET("block/hash/{id}")
    suspend fun fetchBlock(@Path("id") id: String): Block

    @GET("tx")
    suspend fun fetchTransactions(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): PagerModel<Transaction>

    @GET("tx/{hash}")
    suspend fun fetchTransaction(@Path("hash") hash: String): Transaction
}