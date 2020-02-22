package com.example.revolut.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    val baseCurrency: String,
    val rates: Map<String, Float>
)