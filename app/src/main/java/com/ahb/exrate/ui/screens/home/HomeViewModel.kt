package com.ahb.exrate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.repository.CurrencyRateRepository
import com.ahb.exrate.repository.datastore.SelectedAssetsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository,
    private val selectedStore: SelectedAssetsStore
) : ViewModel() {

    // --- Refresh + timestamp state ---
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _lastUpdated = MutableStateFlow(System.currentTimeMillis())
    val lastUpdated: StateFlow<Long> = _lastUpdated.asStateFlow()

    // --- Raw rates from the repository ---
    private val allRates: StateFlow<List<CurrencyItem>> =
        currencyRateRepository.observeRates()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // --- Combined UI state: only show rates whose codes are in selectedStore ---
    val screenState: StateFlow<HomeScreenState> = combine(
        allRates,
        selectedStore.selectedCodes,
        isRefreshing,
        lastUpdated
    ) { rates, selectedCodes, refreshing, lastTs ->
        HomeScreenState(
            items        = rates.filter { it.code in selectedCodes },
            isRefreshing = refreshing,
            lastUpdated  = lastTs
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenState()
        )

    /** User pulled to refresh: re-fetch both fiat & crypto rates */
    fun onPullToRefreshTrigger() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                currencyRateRepository.fetchFiatRates()
                currencyRateRepository.fetchCryptoRates()
                _lastUpdated.value = System.currentTimeMillis()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /** Remove an asset from the selected set */
    fun onRemoveItem(asset: CurrencyItem) {
        viewModelScope.launch {
            val current = selectedStore.selectedCodes.value
            selectedStore.updateSelected(current - asset.code)
        }
    }
}