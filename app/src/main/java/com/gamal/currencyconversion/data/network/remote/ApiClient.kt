package com.gamal.currencyconversion.data.network.remote


import dagger.Provides

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


object  ApiClient {

    fun provideCurrencyApi(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }


    fun provideCurrencyApi2(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}