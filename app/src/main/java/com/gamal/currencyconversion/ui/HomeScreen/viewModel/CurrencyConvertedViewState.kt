package com.gamal.currencyconversion.ui.HomeScreen.viewModel

import com.gamal.currencyconversion.data.model.CurrencyConversion

data class CurrencyConvertedViewState(
    val Loading :Boolean = false,
    val currenciesConverted: List<CurrencyConversion?> = emptyList(),
    val error: String? = null)
