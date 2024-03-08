package jnkadks.namada.namadaexplorer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Block(
    @SerializedName("block_id")
    val blockID: String,

    val header: Header,

    @SerializedName("last_commit")
    val lastCommit: LastCommit,

    @SerializedName("tx_hashes")
    val txHashes: List<TxHash>
) : Parcelable {
    @Parcelize
    data class Header(
        val version: Version,

        @SerializedName("chain_id")
        val chainID: String,

        val height: String,
        val time: String,

        @SerializedName("last_block_id")
        val lastBlockID: BlockID,

        @SerializedName("last_commit_hash")
        val lastCommitHash: String,

        @SerializedName("data_hash")
        val dataHash: String,

        @SerializedName("validators_hash")
        val validatorsHash: String,

        @SerializedName("next_validators_hash")
        val nextValidatorsHash: String,

        @SerializedName("consensus_hash")
        val consensusHash: String,

        @SerializedName("app_hash")
        val appHash: String,

        @SerializedName("last_results_hash")
        val lastResultsHash: String,

        @SerializedName("evidence_hash")
        val evidenceHash: String,

        @SerializedName("proposer_address")
        val proposerAddress: String
    ) : Parcelable

    @Parcelize
    data class BlockID(
        val hash: String,
        val parts: Parts
    ) : Parcelable

    @Parcelize
    data class Parts(
        val total: Long,
        val hash: String
    ) : Parcelable

    @Parcelize
    data class Version(
        val block: String,
        val app: String
    ) : Parcelable

    @Parcelize
    data class LastCommit(
        val height: String,
        val round: String,

        @SerializedName("block_id")
        val blockID: BlockID
    ) : Parcelable

    @Parcelize
    data class TxHash(
        @SerializedName("tx_type")
        val txType: String,

        @SerializedName("hash_id")
        val hashID: String
    ) : Parcelable
}