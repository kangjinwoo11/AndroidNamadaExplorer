package jnkadks.namada.namadaexplorer.common.paging

import android.os.Parcelable
import jnkadks.namada.namadaexplorer.models.PagerModel

interface IDataPagingListener<Data : Parcelable, Model : PagerModel<Data>> {
    suspend fun loadData(page: Int): Model
}