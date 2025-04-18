package com.ahb.exrate.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.ui.components.CurrencyList
import com.ahb.exrate.ui.components.PullToRefreshWrapper

@Composable
fun HomeAssetList(
    items: List<CurrencyItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRemoveItem: (CurrencyItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh    = onRefresh
        ) {
            CurrencyList(
                items        = items,
                onRemoveItem = onRemoveItem
            )
        }
    }
}
