package com.ahb.exrate.model

enum class CurrencyType {
    FIAT,
    CRYPTO
}

data class CurrencyItem(
    val code: String,
    val name: String,
    val rate: String,
    val change: String,
    val type: CurrencyType,
    val isSelected: Boolean = false
)
