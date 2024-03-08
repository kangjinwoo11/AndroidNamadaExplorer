package jnkadks.namada.namadaexplorer.models

import android.os.Parcelable
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val hash: String,

    @SerializedName("block_id")
    val blockID: String,

    @SerializedName("tx_type")
    val txType: TxType,

    @SerializedName("wrapper_id")
    val wrapperID: String,

    @SerializedName("fee_amount_per_gas_unit")
    val feeAmountPerGasUnit: String? = null,

    @SerializedName("fee_token")
    val feeToken: String? = null,

    @SerializedName("gas_limit_multiplier")
    val gasLimitMultiplier: Long? = null,

    val code: String,
    val data: String,

    @SerializedName("return_code")
    val returnCode: Long? = null,

    val tx: Tx? = null
) : Parcelable {

    @Parcelize
    data class Tx(
        @SerializedName("VoteProposal")
        val voteProposal: VoteProposal? = null,

        @SerializedName("Transfer")
        val transfer: Transfer? = null
    ) : Parcelable

    @Parcelize
    data class Transfer(
        val source: String,
        val target: String,
        val token: String,
        val amount: String,
        val shielded: List<Int>? = null
    ) : Parcelable

    @Parcelize
    data class VoteProposal(
        val id: Long,
        val vote: String,
        val voter: String,
        val delegations: List<String>
    ) : Parcelable

    @Parcelize
    enum class TxType(val value: String) : Parcelable {
        @SerializedName("Decrypted")
        Decrypted("Decrypted"),

        @SerializedName("Wrapper")
        Wrapper("Wrapper");
    }
}