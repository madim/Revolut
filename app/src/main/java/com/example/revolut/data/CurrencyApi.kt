package com.example.revolut.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("api/android/latest")
    suspend fun currencyList(@Query("base") base: String): CurrencyResponse
}