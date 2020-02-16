package com.example.revolut.domain

import com.example.revolut.data.CurrencyApi

class RateRepository(
    private val currencyApi: CurrencyApi
) {

    suspend fun rates(base: String): List<Rate> {
        val response = currencyApi.currencyList(base)

        return response.rates.map {
            Rate(it.key, it.value)
        }
    }
}