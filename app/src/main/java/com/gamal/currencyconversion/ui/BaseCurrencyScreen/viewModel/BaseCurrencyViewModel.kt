package com.gamal.currencyconversion.ui.BaseCurrencyScreen.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamal.currencyconversion.data.model.Currency
import com.gamal.currencyconversion.di.AppModule
import com.gamal.currencyconversion.ui.intent.CurrencyViewIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BaseCurrencyViewModel : ViewModel() {
    private val _intentChannel = Channel<CurrencyViewIntent>(Channel.UNLIMITED)
    val intentChannel = _intentChannel.receiveAsFlow()
    var isSearching = mutableStateOf(false)
    private var isSearchStarting = true
    var CurrencyList = mutableStateOf<List<Currency>>(listOf())
    var cachedCurrencyList = listOf<Currency>()

    private val _viewState = MutableStateFlow(BaseCurrencyViewState())
    val viewState: StateFlow<BaseCurrencyViewState> = _viewState.asStateFlow()
    init {
        handleIntents()
    }
    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.collect { intent ->
                when (intent) {
                    is CurrencyViewIntent.getAllCurrency -> fetchCurrencies()
                    is CurrencyViewIntent.getConvertCurrency -> ""
                }
            }
        }
    }



    private fun fetchCurrencies() {
        viewModelScope.launch {
            _viewState.value = BaseCurrencyViewState(Loading = true)
            try {
                val response = AppModule.provideCurrencyApi().getAllCurrency()
                _viewState.value = BaseCurrencyViewState(currencies = response.supportedCurrenciesMap.values.toList())
                 _viewState.value.currencies.forEach { it ->
                     CurrencyList.value+=it
                 }

            } catch (e: Exception) {
                _viewState.value = BaseCurrencyViewState(error = e.localizedMessage)
            }
        }
    }
    fun sendIntent(intent: CurrencyViewIntent) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }

    fun searchPokemonList(query: String) {
        Log.d("ttt",query)
        val listToSearch = if(isSearchStarting) {
            CurrencyList.value
        } else {
            cachedCurrencyList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                CurrencyList.value = cachedCurrencyList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                val code = it.currencyCode
                val name = it.currencyName

                (code?.contains(query.trim(), ignoreCase = true) == true) ||
                        (name?.equals(query.trim(), ignoreCase = true) == true)
            }
            Log.d("ttt",results.size.toString()+"//")

            if(isSearchStarting) {
                cachedCurrencyList = CurrencyList.value
                isSearchStarting = false
            }
            CurrencyList.value = results
            isSearching.value = true
        }
    }

}