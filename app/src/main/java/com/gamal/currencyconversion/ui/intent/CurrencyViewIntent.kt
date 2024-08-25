package com.gamal.currencyconversion.ui.intent

sealed class    CurrencyViewIntent {
    object getAllCurrency :CurrencyViewIntent()
    data class getConvertCurrency(val baseCurrency: String) : CurrencyViewIntent()

}