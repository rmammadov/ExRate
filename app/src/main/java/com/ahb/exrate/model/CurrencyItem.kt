package com.ahb.exrate.model

data class CurrencyItem(
    val code: String,
    val name: String,
    val rate: String,
    val change: String  // e.g. "+0.14%" or "-0.05%"
)
