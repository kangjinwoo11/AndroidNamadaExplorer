package jnkadks.namada.namadaexplorer.uis.composable.pages.home.data

import java.util.Date

data class HomeData(
    val blocksSize: Long,
    val latestBlockDate: Date?,
    val network: String,
    val validatorsSize: Int
)
