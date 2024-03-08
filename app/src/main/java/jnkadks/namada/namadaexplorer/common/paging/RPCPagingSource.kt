package jnkadks.namada.namadaexplorer.common.paging

import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.paging.PagingState
import jnkadks.namada.namadaexplorer.models.PagerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RPCPagingSource<Data : Parcelable, Model : PagerModel<Data>>(
    private val callback: IDataPagingListener<Data, Model>
) : PagingSource<Int, Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return withContext(Dispatchers.IO) {
            try {
                val page = params.key ?: 1
                val responses = callback.loadData(page = page)
                LoadResult.Page(
                    data = responses.data,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (responses.total <= responses.data.size) null else page + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }
}