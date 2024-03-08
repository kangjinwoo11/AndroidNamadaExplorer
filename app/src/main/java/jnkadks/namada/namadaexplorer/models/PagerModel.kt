package jnkadks.namada.namadaexplorer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagerModel<Data : Parcelable>(
    val data: List<Data>,
    val total: Long
) : Parcelable
