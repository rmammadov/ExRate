package com.ahb.exrate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.repository.CurrencyRateRepository
import com.ahb.exrate.repository.datastore.SelectedAssetsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository,
    private val selectedStore: SelectedAssetsStore
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(true)    // start “loading”
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _lastUpdated = MutableStateFlow(System.currentTimeMillis())
    val lastUpdated: StateFlow<Long> = _lastUpdated.asStateFlow()

    private val allRates = currencyRateRepository
        .observeRates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val screenState: StateFlow<HomeScreenState> = combine(
        allRates,
        selectedStore.selectedCodes,
        isRefreshing,
        lastUpdated
    ) { rates, codes, refreshing, lastTs ->
        HomeScreenState(
            items        = rates.filter { it.code in codes },
            isRefreshing = refreshing,
            lastUpdated  = lastTs
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeScreenState())

    init {
        // Initial load, show progress bar
        viewModelScope.launch {
            try {
                currencyRateRepository.fetchRates()
                _lastUpdated.value = System.currentTimeMillis()
            } finally {
                _isRefreshing.value = false
            }
            // Then background auto-refresh every 5s, without toggling the bar
            while (true) {
                delay(5_000L)
                currencyRateRepository.fetchRates()
                _lastUpdated.value = System.currentTimeMillis()
            }
        }
    }

    fun onPullToRefreshTrigger() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                currencyRateRepository.fetchRates()
                _lastUpdated.value = System.currentTimeMillis()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun onRemoveItem(asset: CurrencyItem) {
        viewModelScope.launch {
            selectedStore.updateSelected(
                selectedStore.selectedCodes.value - asset.code
            )
        }
    }
}
