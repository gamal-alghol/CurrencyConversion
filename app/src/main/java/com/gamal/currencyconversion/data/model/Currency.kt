package com.gamal.currencyconversion.data.model

data class Currency(    val availableFrom: String,
                        val availableUntil: String,
                        val countryCode: String,
                        val countryName: String,
                        val currencyCode: String,
                        val currencyName: String,
                        val icon: String,
                        val status: String)
