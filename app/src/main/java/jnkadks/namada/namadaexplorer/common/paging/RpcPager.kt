package jnkadks.namada.namadaexplorer.common.paging

import android.os.Parcelable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.PagerModel

fun <Data : Parcelable, Model : PagerModel<Data>> rpcPager(
    callback: IDataPagingListener<Data, Model>
) = Pager(
    initialKey = 1,
    config = PagingConfig(
        pageSize = Common.limitPage
    ),
    pagingSourceFactory = {
        RPCPagingSource(
            callback = callback
        )
    }
)