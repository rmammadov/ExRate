package com.ahb.exrate.ui.screens.addasset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.repository.CurrencyRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAssetViewModel @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val ratesFlow: StateFlow<List<CurrencyItem>> =
        currencyRateRepository
            .observeRates()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val popularAssets: StateFlow<List<CurrencyItem>> = ratesFlow
        .map { rates -> rates.filter { it.code != "BTC" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val cryptoAssets: StateFlow<List<CurrencyItem>> = ratesFlow
        .map { rates -> rates.filter { it.code == "BTC" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _selectedAssets = MutableStateFlow<Set<CurrencyItem>>(emptySet())
    val selectedAssets: StateFlow<Set<CurrencyItem>> = _selectedAssets.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            currencyRateRepository.fetchRates()
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
        // TODO: persist selectedAssets or pass back via NavController
    }

    /** Trigger a manual refresh of rates (for pull‑to‑refresh) */
    fun onPullToRefreshTrigger() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                currencyRateRepository.fetchRates()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
