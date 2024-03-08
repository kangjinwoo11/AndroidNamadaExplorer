@file:OptIn(ExperimentalMaterial3Api::class)

package jnkadks.namada.namadaexplorer.uis.composable.components

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

val LazyPagingItems<*>.isLoading: Boolean
    get() {
        return loadState.refresh is LoadState.Loading
    }

@Composable
fun <T : Any> PagingComposable(
    pagingData: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    onReload: (() -> Unit)? = null,
    header: @Composable (LazyItemScope.() -> Unit)? = null,
    footer: @Composable (LazyItemScope.() -> Unit)? = null,
    loading: @Composable LazyItemScope.() -> Unit,
    empty: @Composable LazyItemScope.() -> Unit,
    error: @Composable LazyItemScope.(error: Throwable) -> Unit,
    content: @Composable LazyItemScope.(data: T) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    if (onReload != null) {
        LaunchedEffect(pullToRefreshState.isRefreshing) {
            if (pullToRefreshState.isRefreshing) {
                onReload()
            }
        }

        LaunchedEffect(!pagingData.isLoading) {
            if (pagingData.isLoading) {
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            .wrapContentSize(Alignment.TopCenter)
            .clipToBounds()
    ) {
        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior
        ) {
            if (header != null) {
                item {
                    header()
                }
            }

            when (val dataState = pagingData.loadState.refresh) {
                is LoadState.Loading -> item {
                    loading()
                }

                is LoadState.NotLoading -> if (pagingData[0] == null) item {
                    empty()
                } else items(count = pagingData.itemCount) { index ->
                    content(pagingData[index]!!)
                }

                is LoadState.Error -> item {
                    error(dataState.error)
                }
            }

            if (footer != null) {
                item {
                    footer()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            PullToRefreshContainer(
                state = pullToRefreshState,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}