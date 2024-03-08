package jnkadks.namada.namadaexplorer.networks

import jnkadks.namada.namadaexplorer.models.ListProposals
import retrofit2.http.GET

interface NamadaRedNetwork {
    @GET("governance/proposals")
    suspend fun fetchProposals(): ListProposals
}