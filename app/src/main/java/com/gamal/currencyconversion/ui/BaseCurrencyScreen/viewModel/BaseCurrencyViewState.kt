package com.gamal.currencyconversion.ui.BaseCurrencyScreen.viewModel

import com.gamal.currencyconversion.data.model.Currency

data  class BaseCurrencyViewState (
    val Loading :Boolean = false,
    val currencies: List<Currency> = emptyList(),
    val error: String? = null)
