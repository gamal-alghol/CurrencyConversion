package com.gamal.currencyconversion.data.network.remote

import com.gamal.currencyconversion.data.model.ConvertBaseCurrency
import com.gamal.currencyconversion.data.model.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi
{
    @GET(Endpoints.GET_CURRENCIES)
    suspend fun getAllCurrency(): CurrenciesResponse

    @GET("latest/{currency}")
    suspend fun getConvertCurrency(@Path("currency")currency:String):ConvertBaseCurrency
}