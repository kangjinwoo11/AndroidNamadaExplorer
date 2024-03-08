package jnkadks.namada.namadaexplorer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListValidators(
    val currentValidatorsList: List<CurrentValidator>
) : Parcelable {
    @Parcelize
    data class CurrentValidator(
        val address: String,

        @SerializedName("pub_key")
        val pubKey: PubKey,

        @SerializedName("voting_power")
        val votingPower: Long,

        @SerializedName("proposer_priority")
        val proposerPriority: String,

        @SerializedName("voting_percentage")
        val votingPercentage: Double,

        val moniker: String,

        @SerializedName("operator_address")
        val operatorAddress: String
    ) : Parcelable {
        @Parcelize
        data class PubKey(
            val type: String,
            val value: String
        ) : Parcelable
    }
}
