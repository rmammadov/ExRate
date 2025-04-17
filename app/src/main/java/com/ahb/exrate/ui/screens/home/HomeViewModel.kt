package com.ahb.exrate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahb.exrate.model.CurrencyItem
import com.ahb.exrate.repository.CurrencyRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _lastUpdated  = MutableStateFlow(System.currentTimeMillis())

    // raw items stream
    private val itemsFlow: StateFlow<List<CurrencyItem>> =
        currencyRateRepository.observeRates()
            .stateIn(
                scope         = viewModelScope,
                started       = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val screenState: StateFlow<HomeScreenState> = combine(
        itemsFlow,
        _isRefreshing,
        _lastUpdated
    ) { items, refreshing, lastTs ->
        HomeScreenState(
            items        = items,
            isRefreshing = refreshing,
            lastUpdated  = lastTs
        )
    }
        .stateIn(
            scope         = viewModelScope,
            started       = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenState()
        )

    init {
        viewModelScope.launch {
            _isRefreshing.value = true
            currencyRateRepository.fetchRates()
            _lastUpdated.value   = System.currentTimeMillis()
            _isRefreshing.value = false
        }
    }

    fun onPullToRefreshTrigger() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            currencyRateRepository.fetchRates()
            _lastUpdated.update { System.currentTimeMillis() }
            _isRefreshing.update { false }
        }
    }
}
