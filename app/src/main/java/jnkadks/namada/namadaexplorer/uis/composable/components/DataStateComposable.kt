@file:OptIn(ExperimentalMaterial3Api::class)

package jnkadks.namada.namadaexplorer.uis.composable.components

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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

sealed interface DataState<T> {
    class Loading<T> : DataState<T>
    data class Success<T>(
        val successData: T
    ) : DataState<T>

    class Error<T>(val error: Throwable) : DataState<T>
}

@Composable
fun <T> DataStateComposable(
    dataState: DataState<T>,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    footer: @Composable (ColumnScope.() -> Unit)? = null,
    loading: @Composable ColumnScope.() -> Unit,
    error: @Composable ColumnScope.(error: Throwable) -> Unit,
    content: @Composable ColumnScope.(data: T) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        if (header != null) {
            header()
        }

        when (dataState) {
            is DataState.Loading -> {
                loading()
            }

            is DataState.Success -> {
                content(dataState.successData)
            }

            is DataState.Error -> {
                error(dataState.error)
            }
        }

        if (footer != null) {
            footer()
        }
    }
}

@Composable
fun <T> DataStateComposable(
    dataState: DataState<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    onReload: (() -> Unit),
    header: @Composable (LazyItemScope.() -> Unit)? = null,
    footer: @Composable (LazyItemScope.() -> Unit)? = null,
    loading: @Composable LazyItemScope.() -> Unit,
    error: @Composable LazyItemScope.(error: Throwable) -> Unit,
    content: LazyListScope.(data: T) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    if (onReload != null) {
        LaunchedEffect(pullToRefreshState.isRefreshing) {
            if (pullToRefreshState.isRefreshing) {
                onReload()
            }
        }

        LaunchedEffect(dataState) {
            if (dataState !is DataState.Loading) {
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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

            when (dataState) {
                is DataState.Loading -> item {
                    loading()
                }

                is DataState.Success ->
                    content(dataState.successData)

                is DataState.Error -> item {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DataStateComposable(
    dataState: DataState<List<T>>,
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
    content: @Composable LazyItemScope.(index: Int, data: T) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    if (onReload != null) {
        LaunchedEffect(pullToRefreshState.isRefreshing) {
            if (pullToRefreshState.isRefreshing) {
                onReload()
            }
        }

        LaunchedEffect(dataState) {
            if (dataState !is DataState.Loading) {
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
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

            when (dataState) {
                is DataState.Loading -> item {
                    loading()
                }

                is DataState.Success -> if (dataState.successData.isEmpty()) {
                    item {
                        empty()
                    }
                } else {
                    itemsIndexed(dataState.successData) { index, data ->
                        content(index, data)
                    }
                }

                is DataState.Error -> item {
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