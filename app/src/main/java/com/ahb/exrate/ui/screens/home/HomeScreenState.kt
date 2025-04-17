package com.ahb.exrate.ui.screens.home

import com.ahb.exrate.model.CurrencyItem

data class HomeScreenState(
    val items: List<CurrencyItem> = emptyList(),
    val isRefreshing: Boolean      = false,
    val lastUpdated: Long          = System.currentTimeMillis()
)
