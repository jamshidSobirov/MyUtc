package com.jmdev.myutc.presentation.extensions

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.paging.compose.LazyPagingItems
import androidx.compose.runtime.Composable

fun <T : Any> LazyListScope.items(
    lazyPagingItems: LazyPagingItems<T>, itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}
