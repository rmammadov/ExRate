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

    // raw rates
    private val ratesFlow = currencyRateRepository.observeRates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // current selected codes
    private val selectedCodes = selectedStore.selectedCodes

    // combine to set isSelected
    private val ratesWithSelection = combine(ratesFlow, selectedCodes) { rates, codes ->
        rates.map { it.copy(isSelected = codes.contains(it.code)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // split fiat / crypto
    val fiatAssets: StateFlow<List<CurrencyItem>> = ratesWithSelection
        .map { it.filter { c -> c.type == CurrencyType.FIAT } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cryptoAssets: StateFlow<List<CurrencyItem>> = ratesWithSelection
        .map { it.filter { c -> c.type == CurrencyType.CRYPTO } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // search + refresh
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            currencyRateRepository.fetchRates()
        }
    }

    fun onSearchQueryChanged(q: String) { _searchQuery.value = q }

    fun toggleAsset(asset: CurrencyItem) {
        selectedStore.updateSelected(
            selectedCodes.value.let { set ->
                if (asset.code in set) set - asset.code
                else set + asset.code
            }
        )
    }

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
