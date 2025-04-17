package com.ahb.exrate.repository.datastore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedAssetsStore @Inject constructor() {
    private val _selectedCodes = MutableStateFlow<Set<String>>(emptySet())
    val selectedCodes: StateFlow<Set<String>> = _selectedCodes

    fun updateSelected(codes: Set<String>) {
        _selectedCodes.value = codes
    }
}