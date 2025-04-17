package com.ahb.exrate.repository

import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.model.CurrencyType
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

    // A mix of fiat & crypto for simulation
    private val dummyRatesFiat = listOf(
        // --- Fiat Currencies ---
        CurrencyItem("USD", "US Dollar",        "1.0000",    "+0.02%",  CurrencyType.FIAT),
        CurrencyItem("EUR", "Euro",             "0.8934",    "-0.14%",  CurrencyType.FIAT),
        CurrencyItem("JPY", "Japanese Yen",     "134.67",    "+0.05%",  CurrencyType.FIAT),
        CurrencyItem("GBP", "British Pound",    "0.7723",    "-0.08%",  CurrencyType.FIAT),
        CurrencyItem("AUD", "Australian Dollar","1.5001",    "+0.12%",  CurrencyType.FIAT),
        CurrencyItem("CAD", "Canadian Dollar",  "1.3402",    "-0.04%",  CurrencyType.FIAT),
        CurrencyItem("CHF", "Swiss Franc",      "0.9156",    "+0.03%",  CurrencyType.FIAT)
    )

    private val dummyRatesCrypto = listOf(
        // --- Crypto Coins ---
        CurrencyItem("BTC", "Bitcoin",          "42,356.89", "+1.23%",  CurrencyType.CRYPTO),
        CurrencyItem("ETH", "Ethereum",         "2,750.50",  "+0.85%",  CurrencyType.CRYPTO),
        CurrencyItem("LTC", "Litecoin",         "150.33",    "-0.50%",  CurrencyType.CRYPTO),
        CurrencyItem("XRP", "Ripple",           "0.6200",    "+0.10%",  CurrencyType.CRYPTO),
        CurrencyItem("ADA", "Cardano",          "1.2340",    "+2.05%",  CurrencyType.CRYPTO),
        CurrencyItem("DOGE","Dogecoin",         "0.0750",    "+0.30%",  CurrencyType.CRYPTO),
        CurrencyItem("SOL", "Solana",           "25.4000",   "-1.12%",  CurrencyType.CRYPTO),
        CurrencyItem("DOT", "Polkadot",         "6.5000",    "+0.95%",  CurrencyType.CRYPTO),
        CurrencyItem("BNB", "Binance Coin",     "380.20",    "+0.75%",  CurrencyType.CRYPTO),
        CurrencyItem("MATIC","Polygon",         "1.1500",    "-0.20%",  CurrencyType.CRYPTO)
    )

    /** Simulates a network call to fetch currency rates. */
    suspend fun fetchFiatRates() {
        delay(2_000)     // fake network latency
        _rates.value = dummyRatesFiat
    }

    /** Simulates a network call to fetch currency rates. */
    suspend fun fetchCryptoRates() {
        delay(2_000)     // fake network latency
        _rates.value = dummyRatesCrypto
    }

    /** Returns the Flow of currency items. */
    fun observeRates(): Flow<List<CurrencyItem>> = ratesFlow
}
