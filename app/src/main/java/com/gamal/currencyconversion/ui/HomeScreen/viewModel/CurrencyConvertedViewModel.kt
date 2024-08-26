package com.gamal.currencyconversion.ui.HomeScreen.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamal.currencyconversion.data.model.CurrencyConversion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.gamal.currencyconversion.data.network.remote.ApiClient
import com.gamal.currencyconversion.ui.intent.CurrencyViewIntent

class CurrencyConvertedViewModel  : ViewModel()  {
    private val _intentChannel = Channel<CurrencyViewIntent>(Channel.UNLIMITED)
    val intentChannel = _intentChannel.receiveAsFlow()
    var currencyConvertdList = mutableStateOf<List<CurrencyConversion?>>(listOf())

    private val _viewState = MutableStateFlow(CurrencyConvertedViewState())
    val viewState: StateFlow<CurrencyConvertedViewState> = _viewState.asStateFlow()

    init {
        handleIntents()
    }
    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.collect { intent ->
                when (intent) {

                    is CurrencyViewIntent.getAllCurrency -> ""
                    is CurrencyViewIntent.getConvertCurrency -> getConvertCurrency(intent.baseCurrency)


                }
            }
        }
    }

    private fun getConvertCurrency(baseCurrency: String) {

        viewModelScope.launch {
            _viewState.value = CurrencyConvertedViewState(Loading = true)

            try {

                val response = ApiClient.provideCurrencyApi2().getConvertCurrency(baseCurrency)
                Log.d("Full URL", "URL: $response")
                val currencyConversionList = response.conversionRates.map { entry ->

                    CurrencyConversion(currencyCode = entry.key, conversionRate = entry.value)
                }
                _viewState.value = CurrencyConvertedViewState(currenciesConverted = currencyConversionList)
                currencyConvertdList.value += currencyConversionList

            } catch (e: Exception) {
                Log.d("ttt",e.message.toString())
                _viewState.value = CurrencyConvertedViewState(error = e.localizedMessage)
            }
        }
    }



    fun sendIntent(intent: CurrencyViewIntent) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }


}