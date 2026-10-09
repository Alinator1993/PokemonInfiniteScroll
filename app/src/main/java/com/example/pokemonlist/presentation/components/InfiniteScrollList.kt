package com.example.pokemonlist.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> InfiniteScrollList(
    items: List<T>,
    isLoading: Boolean,
    error: String?,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    loadMoreThreshold: Int = 3,
    itemContent: @Composable (T) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= listState.layoutInfo.totalItemsCount - loadMoreThreshold
        }
    }

    LaunchedEffect(shouldLoadMore, items.size) {
        if (shouldLoadMore && error == null) onLoadMore()
    }

    LazyColumn(modifier = modifier, state = listState) {
        items(items) { item ->
            itemContent(item)
        }

        if (isLoading) {
            item {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        if (error != null) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error)
                    Button(onClick = onLoadMore) { Text("Retry") }
                }
            }
        }
    }
}
