package com.ahb.exrate.repository

import com.ahb.exrate.model.CurrencyItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A simple repository that simulates fetching currency rates from an API.
 */
class CurrencyRateRepository {

    // Backing MutableStateFlow holding the list of currency items.
    private val _rates = MutableStateFlow<List<CurrencyItem>>(emptyList())

    // Exposed Flow for the list of currency items.
    val ratesFlow: Flow<List<CurrencyItem>> = _rates.asStateFlow()

    /**
     * Simulates a network call to fetch currency rates.
     */
    suspend fun fetchRates() {
        // Simulate network delay
        delay(2_000)
        // Update the Flow with a new list of currency items.
        _rates.value = listOf(
            CurrencyItem("USD", "US Dollar", "1.0000", "+0.02%"),
            CurrencyItem("EUR", "Euro", "0.8934", "-0.14%"),
            CurrencyItem("BTC", "Bitcoin", "42,356.89", "+1.23%")
        )
    }

    /** Returns the Flow of currency items. */
    fun observeRates(): Flow<List<CurrencyItem>> = ratesFlow
}
