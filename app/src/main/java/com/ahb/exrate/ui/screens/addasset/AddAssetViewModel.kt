package com.ahb.exrate.ui.screens.addasset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.model.CurrencyType
import com.ahb.exrate.repository.CurrencyRateRepository
import com.ahb.exrate.repository.datastore.SelectedAssetsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAssetViewModel @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository,
    private val selectedStore: SelectedAssetsStore
) : ViewModel() {

    // --- Search query state ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- Unified rates flow from repository ---
    private val ratesFlow: StateFlow<List<CurrencyItem>> =
        currencyRateRepository
            .observeRates()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // --- Fiat assets (type == FIAT) ---
    val fiatAssets: StateFlow<List<CurrencyItem>> = ratesFlow
        .map { rates -> rates.filter { it.type == CurrencyType.FIAT } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // --- Crypto assets (type == CRYPTO) ---
    val cryptoAssets: StateFlow<List<CurrencyItem>> = ratesFlow
        .map { rates -> rates.filter { it.type == CurrencyType.CRYPTO } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // --- User’s selection set ---
    private val _selectedAssets = MutableStateFlow<Set<CurrencyItem>>(emptySet())
    val selectedAssets: StateFlow<Set<CurrencyItem>> = _selectedAssets.asStateFlow()

    // --- Pull‑to‑refresh state ---
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        // Initial load: fetch both fiat and crypto rates
        viewModelScope.launch {
            currencyRateRepository.fetchFiatRates()
            currencyRateRepository.fetchCryptoRates()
        }
    }

    /** Update search query as user types */
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /** Toggle presence of an asset in the selection set */
    fun toggleAsset(asset: CurrencyItem) {
        _selectedAssets.update { current ->
            if (current.contains(asset)) current - asset
            else current + asset
        }
    }

    /** Called when user taps “Done” */
    fun confirmSelection() {
        selectedStore.updateSelected(_selectedAssets.value.map { it.code }.toSet())
    }

    /** Trigger a manual refresh of rates (for pull‑to‑refresh) */
    fun onPullToRefreshTrigger() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                currencyRateRepository.fetchFiatRates()
                currencyRateRepository.fetchCryptoRates()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
