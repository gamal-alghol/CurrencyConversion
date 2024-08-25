package com.gamal.currencyconversion.di

import com.gamal.currencyconversion.data.network.remote.Endpoints
import com.gamal.currencyconversion.data.network.remote.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {
    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi2(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}