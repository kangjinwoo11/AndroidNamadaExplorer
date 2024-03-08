package jnkadks.namada.namadaexplorer.uis.composable.pages.home.data

import androidx.annotation.DrawableRes
import jnkadks.namada.namadaexplorer.R

enum class HomeState(
    val title: String,
    @DrawableRes val icon: Int,
) {
    VALIDATORS("Validators", R.drawable.ic_users),
    BLOCKS("Blocks", R.drawable.ic_block),
    TRANSACTIONS("Transactions", R.drawable.ic_transactions),
    PROPOSALS("Proposals", R.drawable.ic_proposals),
    PARAMETERS("Parameters", R.drawable.ic_paramters);

    val des: String
        get() {
            return name
        }
}